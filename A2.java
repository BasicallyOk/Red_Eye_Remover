package a2;

import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.util.*;

public class A2 {
	/**
	 * The original image
	 */
	private static Picture orig;
	
	/**
	 * The image viewer class
	 */
	private static A2Viewer viewer;
	
	/**
	 * Returns a 300x200 image containing the Queen's flag (without the crown).
	 * 
	 * @return an image containing the Queen's flag
	 */
	public static Picture flag() {
		Picture img = new Picture(300, 200);
		int w = img.width();
		int h = img.height();

		// set the pixels in the blue stripe
		Color blue = new Color(0, 48, 95);
		for (int col = 0; col < w / 3; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, blue);
		    }
		}

		// set the pixels in the yellow stripe
		Color yellow = new Color(255, 189, 17);
		for (int col = w / 3; col < 2 * w / 3; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, yellow);
		    }
		}

		// set the pixels in the red stripe
		Color red = new Color(185, 17, 55);
		for (int col = 2 * w / 3; col < w; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, red);
		    }
		}
		return img;
	}

	public static Picture copy(Picture p) {
		Picture result = new Picture(p.width(), p.height());
		// complete the method 

		int w = result.width(); 
		int h = result.height();
		
		// Loops thru every pixel of the image and set the color of the corresponding pixel to the result
		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				result.set(col, row, p.get(col, row));
			}
		}
		
		return result;
		
	}
	
	// ADD YOUR METHODS HERE
	public static Picture border(Picture p, int t) {
		// Copy the original image into sth we're going to work with
		Picture result = A2.copy(p);
		Color borderColor = Color.BLUE;
		int w = result.width();
		int h = result.height();
		
		if (t < 1) {
			throw new IllegalArgumentException("Argument t has to be larger or equal to 1");
		}
		
		for (int col = 0; col < t; col++) {
			// Vertical Borderline
			for (int row = 0; row < h; row++) {
				result.set(col, row, borderColor);
				result.set(w-col-1, row, borderColor);
			}
			// Horizontal Borderline
			// Basically swapping col and row here to avoid having to loop twice
			for (int row = 0; row < w; row++) {
				result.set(row, col, borderColor);
				result.set(row, h-col-1, borderColor);
			}	
		}
		
		// The test code belows will not show Blue at t = 1 if the code is correct
		//System.out.println(result.get(1, 5));
		
		return result;
	}
	
	
	public static Picture grayScale(Picture p) {
		Picture result = new Picture(p.width(),p.height());
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				int r = p.get(col, row).getRed();
				int g = p.get(col, row).getGreen();
				int b = p.get(col, row).getBlue();
				long y = Math.round(0.2989*r + 0.5870*g + 0.1140*b);
				result.set(col, row, new Color((int)y,(int)y,(int)y));
			}
		}
		
		return result;
	}
	
	
	public static Picture binary(Picture p, Color c1, Color c2) {
		Picture result = A2.grayScale(p);
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				if (result.get(col, row).getRed() < 128) {
					result.set(col, row, c1);
				}
				else {
					result.set(col, row, c2);
				}
			}
		}
		
		return result;
	}
	
	
	public static Picture flipVertical(Picture p) {
		Picture result = new Picture(p.width(),p.height());
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				result.set(col, row, p.get(p.width()-col-1, p.height()-row-1));
			}
		}
		return result;
	}
	
	
	public static Picture rotateRight(Picture p) {
		Picture result = new Picture(p.height(), p.width());
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				result.set(col, row, p.get(row, p.height()-col-1));
			}
		}
		
		return result;
	}
	
	
	public static Picture redEye(Picture p) {
        Picture result = copy(p);
        
        // Loop through 
        for (int col = 0; col < result.width(); col++) {
            for (int row = 0; row < result.height(); row++) {
                double r = p.get(col, row).getRed();
                double g = p.get(col, row).getGreen();
                double b = p.get(col, row).getBlue();
                // Only if the color is predominantly red will it be chosen
                double redIntensity = r / ((g+b)/2); 
                if (redIntensity > 2.6) {
                    result.set(col, row, Color.BLACK);
                }
            }
        }
        return result;
    }
	
	
	public static Picture redEyeEpic(Picture p) {
		Picture mask = A2.copy(p);
		Picture result = A2.copy(p);
		// The program will search for boxes of this color. This box should be used as a starting point
		int searchRadius = 4;
		
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				int r = p.get(col, row).getRed();
				int g = p.get(col, row).getGreen();
				int b = p.get(col, row).getBlue();
				// Index on how likely a color should be considered red
				long redish = r - ((g+b)/2);
				if(redish < 0) {redish = 0;}
				mask.set(col, row, new Color((int)redish,(int)redish,(int)redish));
			}
		}
		HashMap<List<Integer>, Integer> colorLocation = new HashMap<List<Integer>, Integer>();
		// Just place holders that can be changed. I'm sorry, I'm new to the language;
		List<Integer> coords = Arrays.asList(new Integer[]{0, 0});
		// Looking for the 2 most intense red spots
		List<Integer> first = Arrays.asList(new Integer[]{0, 0});
		
		for(int col = 0; col < result.width(); col++) {
			for(int row = 0; row < result.height(); row++) {
				coords.set(0, col);
				coords.set(1, row);
				// System.out.println(first.toString());
				// System.out.println(second.toString());
				int redIntensity = A2.box(mask, searchRadius, col, row).getRed();
				colorLocation.put(List.copyOf(coords), redIntensity);
				if (redIntensity < 55) {
					continue;
				}
				try {
					if(redIntensity > colorLocation.get(first)) {
						first.set(0, col);
						first.set(1, row);
					}
				} catch (Exception e) {
					// Nothing will happen as nothing is in hashmap yet
					System.out.println("nothing");
				}
			}
		}
		if(first.get(0) == 0 && first.get(1) == 0) {
			return result;
		}
		mask.set(first.get(0), first.get(1), Color.BLUE);
		for(int radius = searchRadius; radius > 0; radius++) {
			int red = A2.box(mask, radius, first.get(0), first.get(1)).getRed();
			if(red < 0.5*colorLocation.get(first)) {
				searchRadius = radius;
				break;
			}
		}
		
		for(int col = first.get(0)-searchRadius - (int)0.75*searchRadius; col < (first.get(0)+searchRadius+(int)0.75*searchRadius); col++) {
			for(int row = first.get(1)-searchRadius-(int)0.75*searchRadius; row < (first.get(1)+searchRadius+(int)0.75*searchRadius); row++) {
				double r = p.get(col, row).getRed();
                double g = p.get(col, row).getGreen();
                double b = p.get(col, row).getBlue();
                // Only if the color is predominantly red will it be chosen
                double redIntense = r / ((g+b)/2); 
                if (redIntense > 1.2) {
                    result.set(col, row, Color.BLACK);
                }
			}
		}

		return result;
	}
	
	
	public static Picture redEyeAdvanced(Picture p) {
		/*
		 * I am sorry but this code does not work on the cat.
		 */
		// This code will be slightly over-engineered to achieve the best possible result
		Picture result = A2.copy(p);
		// A mask meant to correct and feather some mistakes that might have been made
		Picture mask = new Picture(p.width(), p.height());
		// We are going to use this to determine the final mask
		Picture grayScaled = A2.grayScale(p);
		// A binary picture will be used to cross reference with the mask
		Picture binaryPic = A2.binary(p, Color.BLACK, Color.WHITE);
		
		// 2 ArrayLists to store the location of the eye
		// This will lower execution time during mask adjustment and application
		ArrayList<Integer> eyeX = new ArrayList<Integer>();
		ArrayList<Integer> eyeY = new ArrayList<Integer>();
		
		// How many pixels outside of the mask zone should be considered for adjustment
		int featherValue = 0;
		
		// These will be added back in during feather if the eye is in fact there
		for(int col = 3; col < p.width()-featherValue-3; col++) {
			for(int row = 3; row < p.height()-featherValue-3; row++) {
				Color color = result.get(col, row);
				// These has to be doubles so it wont be rounded up after division
				double r = color.getRed();
				double g = color.getGreen();
				double b = color.getBlue();
				
				// If feathering isnt an issue, stop after the commented line
				if(r/((g+b)/2) > 2.6){
					mask.set(col, row, Color.BLACK);
					eyeX.add(col);
					eyeY.add(row);
					//result.set(col, row, color.BLACK);
				} else {mask.set(col, row, Color.WHITE);}
			}
		}
		int lowerX = Collections.min(eyeX) - featherValue;
		int lowerY = Collections.min(eyeY) - featherValue;
		int higherX = Collections.max(eyeX) + featherValue;
		int higherY = Collections.max(eyeY) + featherValue;
		// System.out.println(lowerX);
		// System.out.println(higherX);
		// The projected diameter of the eye
		int XEye = (int)((higherX - lowerX)/8.7);
		
		// Mask adjustment. I'm betting on the fact that the eyes arent near the border.
		for(int col = lowerX; col < (lowerX + XEye); col++) {
			for(int row = lowerY; row < higherY; row++) {
				// Left eye adjustment
				if (mask.get(col, row).equals(Color.WHITE)) {
					if(65 < grayScaled.get(col, row).getRed() && grayScaled.get(col, row).getRed() < 250 && binaryPic.get(col, row).equals(Color.BLACK)) {
						mask.set(col, row, grayScaled.get(col, row));
					}
				}
				if (!mask.get(col, row).equals(Color.WHITE)) {
					result.set(col, row, mask.get(col, row));
				}
				if (mask.get(col+8*XEye, row).equals(Color.WHITE)) {
					if(50 < grayScaled.get(col+8*XEye, row).getRed() && grayScaled.get(col+8*XEye, row).getRed() < 250 && binaryPic.get(col+8*XEye, row).equals(Color.BLACK)) {
						mask.set(col+8*XEye, row, grayScaled.get(col+8*XEye, row));
					}
				}
				if (!mask.get(col+8*XEye, row).equals(Color.WHITE)) {
					result.set(col+8*XEye, row, mask.get(col+8*XEye, row));
				}
			}
		}
		result = A2.redEye(result);
		
		return result;
	}
	
	
	public static Picture blur(Picture p, int radius) {
        Picture result = new Picture(p.width(), p.height());
        for (int col = 0; col < result.width(); col++) {
            for (int row = 0; row < result.height(); row++) {
                result.set(col, row, box(p, radius, col, row));
            }
        }
        return result;
    }
    
    public static Color box(Picture p, int radius, int col, int row) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int counter = 0;
        for (int boxCol = col - radius; boxCol < col + radius + 1; boxCol++) {
            for (int boxRow = row - radius; boxRow < row + radius + 1; boxRow++) {
                
                if (boxCol < 0) {
                    boxCol = 0;
                } else if (boxCol > p.width() - 1) {
                    break;
                }
                
                if (boxRow < 0) {
                    boxRow = 0;
                } else if (boxRow > p.height() - 1) {
                    break;
                }
                
                Color rgb = p.get(boxCol, boxRow);
                red += rgb.getRed();
                green += rgb.getGreen();
                blue += rgb.getBlue();
                counter ++;
            }
        }
        
        red = Math.round(red/counter);
        green = Math.round(green/counter);
        blue = Math.round(blue/counter);
        
        Color result = new Color(red, green, blue);
        return result;
    }
	
	/**
	 * A2Viewer class calls this method when a menu item is selected.
	 * This method computes a new image and then asks the viewer to
	 * display the computed image.
	 * 
	 * @param op the operation selected in the viewer
	 */
	public static void processImage(String op) {
		
		switch (op) {
		case A2Viewer.FLAG:
			// create a new image by copying the original image
			Picture p = A2.flag();
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.COPY:
			// create a new image by copying the original image
			p = A2.copy(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_1:
			// create a new image by adding a border of width 1 to the original image
			p = A2.border(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_5:
			// create a new image by adding a border of width 5 the original image
			p = A2.border(A2.orig, 5);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_10:
			// create a new image by adding a border of width 10  the original image
			p = A2.border(A2.orig, 10);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.TO_GRAY:
			// create a new image by converting the original image to grayscale
			p = A2.grayScale(A2.orig);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.TO_BINARY:
			// create a new image by converting the original image to black and white
			p = A2.binary(A2.orig, Color.BLACK, Color.WHITE);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.FLIP_VERTICAL:
			// create a new image by flipping the original image vertically
			p = A2.flipVertical(A2.orig);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.ROTATE_RIGHT:
			// create a new image by rotating the original image to the right by 90 degrees
			p = A2.rotateRight(A2.orig);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.RED_EYE:
			// create a new image by removing the redeye effect in the original image
			/* You can use either redEye(): the normal algorithm
			or redEyeAdvanced(): use grayscaled image of the eye to patch onto red spots for a natural looking eye (only works for the girl)
			or redEyeEpic(): find out exactly where the eye is, draw a box that approximate its location, and then apply a strong red filter
			redEyeEpic depends on an eye detection program which does not give the best result without tinkering with other methods,
			so you have to apply it 4 times in order to achieve the best results (if all the red spots have been eliminated the method will skip).
			*/
			p = A2.redEye(A2.orig);
			// p = A2.redEyeEpic(p);
			// p = A2.redEyeEpic(p);
			// p = A2.redEyeEpic(p);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.BLUR_1:
			// create a new image by blurring the original image with a box blur of radius 1
			p = A2.blur(A2.orig, 1);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.BLUR_3:
			// create a new image by blurring the original image with a box blur of radius 3
			p = A2.blur(A2.orig, 3);
			A2.viewer.setComputed(p);
			
			break;
		case A2Viewer.BLUR_5:
			// create a new image by blurring the original image with a box blur of radius 5
			p = A2.blur(A2.orig, 5);
			A2.viewer.setComputed(p);
			
			break;
		default:
			// do nothing
		}
	}
	
	/**
	 * Starting point of the program. Students can comment/uncomment which image
	 * to use when testing their program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		A2.viewer = new A2Viewer();
		A2.viewer.setVisible(true);
		
		
		URL img;
		// uncomment one of the next two lines to choose which test image to use (person or cat)
		// img = A2.class.getResource("redeye-400x300.jpg");   
		img = A2.class.getResource("cat.jpg");
		
		
		A2.orig = new Picture(new File(img.getFile()));
		A2.viewer.setOriginal(A2.orig);
	}

}
