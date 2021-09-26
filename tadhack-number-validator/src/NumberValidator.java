import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;

public class NumberValidator {
	
	public String hasCarrier(String inputNumber, String regionCode) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
		PhoneNumber numberToCheck;
		try {
			numberToCheck = phoneUtil.parse(inputNumber, CountryCodeSource.UNSPECIFIED.name());
			String carrierName = carrierMapper.getNameForNumber(numberToCheck, Locale.forLanguageTag(regionCode));
			if (carrierMapper.getNameForNumber(numberToCheck, Locale.forLanguageTag(regionCode)) != "") {
				return carrierName;
			} else {
				return "false";
			}
		} catch (NumberParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	
	public void processFile(String fileName, String regionCode) {
		File fout = new File(fileName + "_filtered");
		FileOutputStream fos = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if (this.hasCarrier(line, regionCode).equals("false")) {
		    		// Do nothing
				} else {
					bw.write(line);
					bw.newLine();
				}
		    }
		    bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String fileName = args[0];
		String regionCode = args[1];
		
		NumberValidator nc = new NumberValidator();
		nc.processFile(fileName, regionCode);

	}

}
