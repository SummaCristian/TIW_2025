package it.polimi.tiw.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.tiw.DAOs.ImageDAO;
import it.polimi.tiw.DAOs.ItemDAO;
import it.polimi.tiw.beans.Item;
import it.polimi.tiw.exceptions.MissingParametersException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig
@WebServlet("/CreateItem")
public class CreateItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection
	private Connection connection = null;
	
	// Initializes the Thymeleaf Template Engine, used to render the HTML page, and the DB connection
    @Override
    public void init() {
    	// Initializes the Database connection
    	try {
			connection = EnvUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // Forwards back without doing anything
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getRequestDispatcher("/sell").forward(request, response);
    }


	/*
	 * Checks if the required Data is ok, then creates the new Item with it.
	 * Forwards back to the Sell page with errors if there is any.
	 * Forwards back to the Sell page with no error if everything is ok. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Reading data from Request
		String itemName = request.getParameter("itemName");
		String itemDescription = request.getParameter("itemDescription");
		String itemPriceParam = request.getParameter("price");
		String creatorIdParam = request.getParameter("creatorId");
		Part imagePart = request.getPart("image");
		
		// Checking the data
		try {
			checkData(
				itemName,
				itemDescription,
				itemPriceParam,
				creatorIdParam,
				imagePart
			);
		} catch (MissingParametersException e) {
			// Missing some data
			
			// Return the User to the same page, with an error message
			request.setAttribute("itemError", "Missing parameters. Please try again...");
			
			request.getRequestDispatcher("/sell").forward(request, response);
			
			return;
		}
		
		// Everything is ok, continue
		
		// Parse Integer Data from Strings
		int itemPrice =  Integer.parseInt(itemPriceParam);
		int creatorId = Integer.parseInt(creatorIdParam);
        
        try {
        	// Adds the Item to the Database (without Image)
        	Item item = new Item();
        	item.setItemName(itemName);
        	item.setItemDescription(itemDescription);
        	item.setPrice(itemPrice);
        	// Image is null
        	item.setCreatorId(creatorId);
        	
        	ItemDAO itemDao = new ItemDAO(connection);
        	
        	int itemId = 0;
        	
        	try {
        		itemId = itemDao.insert(item);
        	} catch (SQLException e) {
        		// Something went wrong with the INSERT for the Item
        		e.printStackTrace();
        	}
        	
        	// Item inserted correctly, now it needs to save the Image
        	
        	// Generate unique filename based on Item ID
        	String originalFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        	String extension = "";
        	int dotIndex = originalFileName.lastIndexOf('.');
        	if (dotIndex >= 0) {
        	    extension = originalFileName.substring(dotIndex);
        	} else {
        		// Missing extension, failsafe
        		extension = ".png";
        	}
        	
        	String fileName = "item_" + itemId + extension;
            
            // Saving Image
            String uploadPath = getServletContext().getRealPath("/images/uploaded_images");

            // Checks the saving directory
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
            	// Folder doesn't exist, creating it now
                uploadDir.mkdirs();
            }

            String fullImagePath = uploadPath + File.separator + fileName;
            
            try (InputStream fileContent = imagePart.getInputStream()) {
            	// Write the File into the Folder
                Files.copy(fileContent, Paths.get(fullImagePath), StandardCopyOption.REPLACE_EXISTING);
            }
            
            String relativePath = "images/uploaded_images/" + fileName;
            
            ImageDAO imageDao = new ImageDAO(connection);
            
            // Saves the Image into the Database
        	int imageId = imageDao.insert(fileName, relativePath);
        	
        	// Updates the Item to include the Image's ID
        	itemDao.addImageToItem(itemId, imageId);
        	
        	// Returns the User to the Sell Page
        	response.sendRedirect(request.getContextPath() + "/sell");
        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * Checks if all the data is here or if something is missing
	 * Throws MissingParametersException if something is missing
	 * Returns nothing if everything is ok
	 */
	private void checkData(
			String itemName,
			String itemDescription,
			String itemPriceParam,
			String creatorIdParam,
			Part imagePart
	) throws MissingParametersException {
		if (itemName == null || itemName.isBlank() 
				|| itemDescription == null || itemDescription.isBlank() 
				|| itemPriceParam == null || itemPriceParam.isBlank() 
				||	creatorIdParam == null || creatorIdParam.isBlank()
				|| imagePart == null
		) {
			throw new MissingParametersException();
		}
	}
	
	/*
	 * Destroys the Servlet object, closing the Connection while doing so.
	 */
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
