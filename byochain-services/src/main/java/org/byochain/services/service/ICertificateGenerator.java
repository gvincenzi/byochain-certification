package org.byochain.services.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.byochain.model.entity.Block;

/**
 * This service (reserved to Administrator) generates a certificate with informations about a Block and its miner
 * @author Giuseppe Vincenzi
 *
 */
public interface ICertificateGenerator {
	byte[] createCertificate(Block block) throws IOException;
}
