/**
 * 
 */
package org.byochain.services.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.byochain.model.entity.Block;
import org.byochain.services.service.ICertificateGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ICertificateGenerator}
 * 
 * @author Giuseppe Vincenzi
 *
 */
@Service
public class CertificateGenerator implements ICertificateGenerator {

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 */
	@Override
	public byte[] createCertificate(Block block) throws IOException {
		PDDocument document = new PDDocument();
		PDPage page1 = new PDPage(PDRectangle.A4);
		PDRectangle rect = page1.getMediaBox();
		document.addPage(page1);

		PDFont fontPlain = PDType1Font.COURIER;
		PDFont fontBold = PDType1Font.COURIER_BOLD;

		PDPageContentStream cos = new PDPageContentStream(document, page1);

		int line = 0;

		InputStream initialStream = new ClassPathResource("assets/byochain.png").getInputStream();
		File resource = new File(System.currentTimeMillis()+".tmp");

		java.nio.file.Files.copy(initialStream, resource.toPath(), StandardCopyOption.REPLACE_EXISTING);

		PDImageXObject ximage = PDImageXObject.createFromFileByContent(resource, document);
		float scale = 0.3f;
		cos.drawImage(ximage, 20, rect.getHeight() - 50 * (++line) - ximage.getHeight() * scale,
				ximage.getWidth() * scale, ximage.getHeight() * scale);

		cos.beginText();
		cos.setFont(fontBold, 16);
		cos.newLineAtOffset(20, rect.getHeight() - 50 * (++line) - ximage.getHeight() * scale);
		cos.showText("Certificate of inscription in the blockchain");
		cos.endText();

		float bodyOffset = rect.getHeight() - 100 - ximage.getHeight() * scale;

		cos.moveTo(20, bodyOffset - 20 * (++line));
		cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
		cos.stroke();

		// BLOCK INFOS

		cos.beginText();
		cos.setFont(fontBold, 12);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Block Informations");
		cos.endText();

		cos.moveTo(20, bodyOffset - 20 * (++line));
		cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
		cos.stroke();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Issued to : " + block.getData().getData());
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Block Hash : " + block.getHash());
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Previous Block Hash : " + block.getPreviousHash());
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Expiration date : " + block.getData().getExpirationDate().getTime());
		cos.endText();

		// MINER INFOS
		cos.moveTo(20, bodyOffset - 20 * (++line));
		cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
		cos.stroke();

		cos.beginText();
		cos.setFont(fontBold, 12);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Miner Informations");
		cos.endText();

		cos.moveTo(20, bodyOffset - 20 * (++line));
		cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
		cos.stroke();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("User Identification Number : " + block.getMiner().getUserId());
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 8);
		cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
		cos.showText("Username : " + block.getMiner().getUsername());
		cos.endText();

		if (StringUtils.isNotBlank(block.getMiner().getTemporaryPassword())) {
			cos.beginText();
			cos.setFont(fontPlain, 8);
			cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
			cos.showText("Password : " + block.getMiner().getTemporaryPassword());
			cos.endText();

			cos.moveTo(20, bodyOffset - 20 * (++line));
			cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
			cos.stroke();

			cos.beginText();
			cos.setFont(fontBold, 12);
			cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
			cos.showText("Parameters to embed widget");
			cos.endText();

			cos.moveTo(20, bodyOffset - 20 * (++line));
			cos.lineTo(rect.getWidth() - 20, bodyOffset - 20 * (line));
			cos.stroke();

			cos.beginText();
			cos.setFont(fontPlain, 8);
			cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
			cos.showText("Hash : " + block.getHash());
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 8);
			cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
			cos.showText("Username : " + block.getMiner().getUsername());
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 8);
			cos.newLineAtOffset(20, bodyOffset - 20 * (++line));
			cos.showText("Password : " + block.getMiner().getTemporaryPassword());
			cos.endText();
		}

		// FOOTER
		cos.moveTo(20, 100);
		cos.lineTo(rect.getWidth() - 20, 100);
		cos.stroke();

		cos.beginText();
		cos.setFont(fontPlain, 6);
		cos.newLineAtOffset(rect.getWidth() - 200, 80);
		cos.showText("On behalf of the customer (authorized signature)");
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 6);
		cos.newLineAtOffset(20, 80);
		cos.showText("On behalf of the company (authorized signature) ");
		cos.endText();

		cos.beginText();
		cos.setFont(fontPlain, 6);
		cos.newLineAtOffset(rect.getWidth() - 200, 50);
		cos.showText("Date of mining " + block.getTimestamp().getTime());
		cos.endText();

		cos.close();

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		document.save(output);
		document.close();
		resource.delete();
		return output.toByteArray();
	}
}
