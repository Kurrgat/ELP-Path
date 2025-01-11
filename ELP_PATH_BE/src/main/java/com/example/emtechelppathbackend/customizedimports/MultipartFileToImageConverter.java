package com.example.emtechelppathbackend.customizedimports;

import com.example.emtechelppathbackend.image.Image;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Component
public class MultipartFileToImageConverter extends AbstractConverter<MultipartFile, Image> {
	  @Override
	  protected Image convert(MultipartFile source) {
		    if (source == null || source.isEmpty()) {
				return null;
		    }

		    Image image = new Image();
		    image.setName(source.getOriginalFilename());
		    try {
				byte[] imageBytes = source.getBytes();
				String base64ImageString = Base64.getEncoder().encodeToString(imageBytes);
				image.setData(base64ImageString);
				image.setType(source.getContentType());
		    } catch (IOException e) {
				throw new RuntimeException("Error when handling the image conversion");
		    }

		    return image;
	  }
}
