package br.app.cashew.feature01.authentication.util.email;

public class EmailConfirmationEmailProperties {

    private EmailConfirmationEmailProperties() {}
    private static final String SENDER = "noreply@cashew.app.br";
    private static final String SUBJECT = "[Conta Cashew] - Solicitação de mudança de E-mail";
    private static final String TEXT =
            """
            <html>
            <body>
                <p>Esta mensagem é gerada automaticamente pelo Cashew.</p>
                <p>--------------------------------------------</p>
                <p>Obrigado por usar uma conta Cashew. Foi solicitado uma mudança de email na sua conta, por favor insira este PIN:</p>
                <p>%s</p>
                <p>Caso não tenha sido você que solicitou essa mudança de e-mail, por favor, ignore.</p>
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
