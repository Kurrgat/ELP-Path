package com.example.emtechelppathbackend.attachabledata;

import com.example.emtechelppathbackend.image.Image;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//class to be used in sending images in emails, specifically to extract name and data
public class ImageAttachableData implements AttachableData{

	  private final Image image;
	  @Override
	  public String getName() {
		    return image.getName();
	  }

	  @Override
	  public String getData() {
		    return image.getData();
	  }
}
