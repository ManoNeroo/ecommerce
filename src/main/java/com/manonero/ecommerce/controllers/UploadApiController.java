package com.manonero.ecommerce.controllers;

import com.manonero.ecommerce.models.Response;
import com.manonero.ecommerce.models.StorageFileNotFoundException;
import com.manonero.ecommerce.services.IStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class UploadApiController {
	@Autowired
	private IStorageService storageService;

	@GetMapping("/upload/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		String fileNameLower = filename.toLowerCase();
		if (fileNameLower.endsWith(".png")) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
		} else if (fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg")) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
		} else if (fileNameLower.endsWith(".gif")) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_GIF).body(file);
		} else {
			return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(file);
		}
	}

	@PostMapping("/api/upload")
	public Response handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		String fileName = storageService.store(file);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return new Response(fileName, true);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
