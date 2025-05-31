package it.polimi.tiw.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		// Get the requested path after "/images/"
		String imagePath = request.getPathInfo(); // e.g. /uploaded_images/item_64.jpg

		if (imagePath == null || imagePath.equals("/") || !imagePath.startsWith("/uploaded_images/")) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image path");
		    return;
		}

		// Remove the prefix "/uploaded_images/" to get the actual file name
		String imageName = imagePath.replaceFirst("/uploaded_images/", "");

        // Locate the image file
        File imageFile = new File(EnvUtil.getUploadDir(), imageName);
                
        if (!imageFile.exists() || !imageFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }

        // Set content type based on file extension
        String mimeType = getServletContext().getMimeType(imageFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(imageFile.length());

        // Stream the image to the client
        try (InputStream in = new FileInputStream(imageFile);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}