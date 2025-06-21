package com.springboot.lms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.lms.model.Author;
import com.springboot.lms.model.UserInfo;
import com.springboot.lms.repository.AuthorRepository;
import com.springboot.lms.repository.UserInfoRepository;
@Service
public class AuthorService {
	
	AuthorRepository ar;
	UserInfoRepository uir;
	PasswordEncoder pe;
	Logger logger = LoggerFactory.getLogger(AuthorService.class);
	

	public AuthorService(AuthorRepository ar, UserInfoRepository uir, PasswordEncoder pe) {
		super();
		this.ar = ar;
		this.uir = uir;
		this.pe = pe;
	}



	public Author addAuthor(Author author) {
		UserInfo userInfo = author.getUser();
		String encodedPassword = pe.encode(userInfo.getPassword());
		userInfo.setPassword(encodedPassword);
		userInfo.setRole("AUTHOR");
		author.setUser(uir.save(userInfo));
		author.setActive(true);
		return ar.save(author);
	}



	public Author uploadProfilePic(MultipartFile file, Principal principal) throws IOException {
		 /* Fetch Author Info by username */
        Author author = ar.getByUsername(principal.getName());
        logger.info("This is author --> " + author.getName());
        /* extension check: jpg,jpeg,png,gif,svg : */
        String originalFileName = file.getOriginalFilename(); // profile_pic.png
        logger.info(originalFileName.getClass().toString());

        logger.info("" + originalFileName.split("\\.").length);
        String extension = originalFileName.split("\\.")[1]; // png
        if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            logger.error("extension not approved " + extension);
            throw new RuntimeException("File Extension " + extension + " not allowed " + "Allowed Extensions"
                    + List.of("jpg", "jpeg", "png", "gif", "svg"));
        }
        logger.info("extension approved " + extension);
        /* Check the file size */
        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            logger.error("File oversize " + kbs);
            throw new RuntimeException("Image Oversized. Max allowed size is " + kbs);
        }
        logger.info("Profile Image Size " + kbs + " KBs");

        /* Check if Directory exists, else create one */
        String uploadFolder = "C:\\Users\\Shiva vardhan reddy\\OneDrive\\Desktop\\hexaware\\FSD - PHASE - III\\Class Work\\React\\react-lms-ui\\public\\profile_pics";
        Files.createDirectories(Path.of(uploadFolder));
        logger.info(Path.of(uploadFolder) + " directory ready!!!");
        /* Define the full path */
        Path path = Paths.get(uploadFolder, "\\", originalFileName);
        /* Upload file in the above path */
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        /* Set url of file or image in author object */
        author.setProfilePic(originalFileName);
        /* Save author Object */
        return ar.save(author);
	}



	

}
