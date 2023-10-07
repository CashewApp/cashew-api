package br.app.cashew.feature01.authentication.util.email;

public class ForgotPasswordEmailProperties {

    private ForgotPasswordEmailProperties() {}
    private static final String SENDER = "noreply@cashew.app.br";
    private static final String SUBJECT = "[Conta Cashew] - Esqueceu a Senha";
    private static final String TEXT =
            """
            <html>
            <body>
                <p>Esta mensagem é gerada automaticamente pelo Cashew.</p>
                <hr></hr>
                <p>Obrigado por usar uma conta Cashew. Foi solicitado uma mudança de senha na sua conta, por favor clique nesta URL:</p>
                <p><a href='https://localhost:3000/forgot-password?token=%s'>Trocar a sua senha</a></p>
                <p>Caso não tenha sido você que solicitou essa mudança de senha, por favor, ignore.</p>
                <p>Obrigado,<br>Time Cashew</p>
            </body>
            </html>
            """;

    public static String getSender() {
        return SENDER;
    }

    public static String getSubject() {
        return SUBJECT;
    }

    public static String getText() {
        return TEXT;
    }
}
