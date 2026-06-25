package com.touristsafety.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.touristsafety.entity.User;
import com.touristsafety.exception.BadRequestException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class QrCodeService {

    public byte[] touristQr(User user) {
        String payload = "TOURIST-ID:" + user.getId()
                + "|NAME:" + user.getName()
                + "|PHONE:" + user.getPhone()
                + "|EMERGENCY:" + nullSafe(user.getEmergencyContactPhone());
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(payload, BarcodeFormat.QR_CODE, 260, 260);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException ex) {
            throw new BadRequestException("Could not generate tourist QR code.");
        }
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
