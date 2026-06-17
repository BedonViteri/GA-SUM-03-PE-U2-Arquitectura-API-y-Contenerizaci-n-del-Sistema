package ec.edu.uteq.sga.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Async
    public void enviarCredenciales(String correo, String nombreCompleto,
                                   String username, String password) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(correo);
            helper.setSubject("Bienvenido al SGA — Escuela Provincias Unidas");
            helper.setText(construirHtml(nombreCompleto, username, password), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        }
    }

    private String construirHtml(String nombre, String username, String password) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background-color: #243A76; padding: 24px; text-align: center;">
                    <h1 style="color: white; margin: 0; font-size: 20px;">
                        Escuela de Educación Básica
                    </h1>
                    <p style="color: #a8bce8; margin: 4px 0 0 0; font-size: 14px;">
                        Provincias Unidas — Rcto. San Basilio
                    </p>
                </div>
                <div style="padding: 32px; background: #f8fafc; border: 1px solid #e2e8f0;">
                    <p style="color: #334155; font-size: 15px;">Estimado/a <strong>%s</strong>,</p>
                    <p style="color: #334155; font-size: 14px;">
                        Se ha creado su cuenta en el Sistema de Gestión Académica (SGA).
                        A continuación sus credenciales de acceso:
                    </p>
                    <div style="background: white; border: 1px solid #e2e8f0; border-radius: 8px; padding: 20px; margin: 20px 0;">
                        <table style="width: 100%%;">
                            <tr>
                                <td style="color: #64748b; font-size: 13px; padding: 6px 0;">Usuario:</td>
                                <td style="color: #243A76; font-weight: bold; font-size: 15px;">%s</td>
                            </tr>
                            <tr>
                                <td style="color: #64748b; font-size: 13px; padding: 6px 0;">Contraseña temporal:</td>
                                <td style="color: #243A76; font-weight: bold; font-size: 15px;">%s</td>
                            </tr>
                        </table>
                    </div>
                    <p style="color: #ef4444; font-size: 13px; font-weight: bold;">
                        ⚠️ Por seguridad, deberá cambiar su contraseña en el primer ingreso.
                    </p>
                    <a href="http://localhost:5173/login"
                       style="display: inline-block; background-color: #243A76; color: white;
                              padding: 12px 24px; border-radius: 8px; text-decoration: none;
                              font-size: 14px; margin-top: 8px;">
                        Ingresar al sistema
                    </a>
                </div>
                <div style="padding: 16px; text-align: center; background: #243A76;">
                    <p style="color: #a8bce8; font-size: 12px; margin: 0;">
                        Sistema de Gestión Académica © 2026
                    </p>
                </div>
            </div>
            """.formatted(nombre, username, password);
    }
}