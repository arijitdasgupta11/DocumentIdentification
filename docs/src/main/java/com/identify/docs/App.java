package com.identify.docs;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.Tesseract;

public class App {

	public static boolean validIdNumber(String extractedText, String cardName) 
	{

		boolean matchedPattern = false;
		String idPattern = "";

		switch (cardName) 
		{
		case "Aadhaar":
			idPattern = "\\b(?:\\d{4}\\s?){2}\\d{4}\\b";
			break;
		case "Voter":
			idPattern = "[A-Z]{3}[0-9]{7}";
			break;
		case "Driving":
			idPattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}";
			break;
		case "Pan":
			idPattern="[A-Z]{5}[0-9]{4}[A-Z]{1}";
			break;
		}

		Pattern pattern = Pattern.compile(idPattern);

		Matcher matcher = pattern.matcher(extractedText);

		System.out.println("-----------------ID-NUMBER--------------------");

		while (matcher.find()) 
		{
			matchedPattern = true;
			System.out.println(matcher.group().replaceAll("\\s", ""));
			break;
		}

		System.out.println("-----------------ID-NUMBER--------------------");

		return matchedPattern;
	}

	public static void main(String[] args) 
	{
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the drive name:");
		String drivename = scanner.next();
		System.out.print("\nEnter the folder name:");
		String foldername = scanner.next();
		String path = drivename.toUpperCase() + ":\\" + foldername;

		File folder = new File(path);

		File[] files = folder.listFiles();
		System.out.println("\nTotal number of files: "+files.length);
		
		for (File file : files) 
		{
			System.out.println("File-Name:"+file.getName());
			
			try {
				Tesseract tesseract = new Tesseract();
				System.out.println("Tesseract Object: "+tesseract);
				
				tesseract.setDatapath("C:\\Users\\Arijit\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
				tesseract.setLanguage("eng");

				System.out.println("Before OCR");
				String extractedText = tesseract.doOCR(file);
				
				System.out.println("--------------------------------EXTRACTED TEXT--------------------------------");
				System.out.println(extractedText);
				System.out.println("--------------------------------EXTRACTED TEXT--------------------------------");
				
				String documentType = identifyDocument(extractedText);
				System.out.println("\n--------------------------------RESULT--------------------------------");
				System.out.println("File Name:-" + file.getName() + "\nDocument Type:-" + documentType);
				System.out.println("--------------------------------RESULT--------------------------------\n");
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scanner.close();
	}

	private static String identifyDocument(String extractedText) 
	{

		if (extractedText.contains("DRIVING") && validIdNumber(extractedText, "Driving")) 
		{
			return "Driving License";
		} 
		
		else if (extractedText.contains("Aadhaar") && validIdNumber(extractedText, "Aadhaar")) 
		{
			return "Aadhaar Card";
		} 
		
		else if (extractedText.contains("ELECTION") && validIdNumber(extractedText, "Voter")) 
		{
			return "Voter Card";
		}  
		
		else if (extractedText.contains("Permanent") && validIdNumber(extractedText, "Pan")) 
		{
			return "PAN Card";
		} 
		
		else 
		{
			return "Unidentifiable Document";
		}
	}
}
