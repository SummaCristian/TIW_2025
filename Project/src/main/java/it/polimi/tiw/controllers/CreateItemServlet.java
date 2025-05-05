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
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.beans.Item;
import it.polimi.tiw.exceptions.NoSuchImageException;
import it.polimi.tiw.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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
			connection = DBUtil.getConnection();
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
		int itemPrice = Integer.parseInt(request.getParameter("price"));
		int creatorId = Integer.parseInt(request.getParameter("creatorId"));
		Part imagePart = request.getPart("image");
        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        
        // Saving Image
        String uploadPath = getServletContext().getRealPath("/images/uploaded_images");

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fullImagePath = uploadPath + File.separator + fileName;
        try (InputStream fileContent = imagePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(fullImagePath), StandardCopyOption.REPLACE_EXISTING);
        }
        
        String relativePath = "images/uploaded_images/" + fileName;
        
        ImageDAO imageDao = new ImageDAO(connection);
        try {
			// Saves the Image into the Database
        	int imageId = imageDao.insert(fileName, relativePath);
			
			// Creates the Image Bean
        	Image image = new Image(imageId, fileName, relativePath);
        	
        	// Adds the Item to the Database
        	Item item = new Item();
        	item.setItemName(itemName);
        	item.setItemDescription(itemDescription);
        	item.setPrice(itemPrice);
        	item.setImage(image);
        	item.setCreatorId(creatorId);
        	
        	ItemDAO itemDao = new ItemDAO(connection);
        	
        	try {
        		itemDao.insert(item);
        	} catch (SQLException e) {
        		// Something went wrong with the INSERT for the Item, so we DELETE the Image from its Table
        		try {
					imageDao.delete(item.getImage().getId());
				} catch (NoSuchImageException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
