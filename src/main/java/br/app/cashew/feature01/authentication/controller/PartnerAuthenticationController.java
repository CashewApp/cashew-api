package br.app.cashew.feature01.authentication.controller;

import br.app.cashew.feature01.authentication.dto.RefreshTokenDTO;
import br.app.cashew.feature01.authentication.dto.user.UserLoginDTO;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.service.authentication.BaseAuthenticationService;
import br.app.cashew.feature01.authentication.service.authentication.partner.PartnerAuthenticationServiceImpl;
import br.app.cashew.feature01.authentication.service.token.BaseOAuth2TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth/partner")
public class PartnerAuthenticationController extends BaseAuthenticationController<Partner>{
    private final PartnerAuthenticationServiceImpl partnerAuthenticationServiceImpl;

    @Autowired
    public PartnerAuthenticationController(
            PartnerAuthenticationServiceImpl partnerAuthenticationServiceImpl,
            @Qualifier("partnerOAuth2TokenServiceImpl") BaseOAuth2TokenService baseOAuth2TokenService) {

        super(baseOAuth2TokenService);
        this.partnerAuthenticationServiceImpl = partnerAuthenticationServiceImpl;
    }

    // metodo para cadastrar
    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> registratePartner(@RequestBody @Valid Partner partner /*, DadosBancarios dadosBancarios*/) {

        return super.registrate(partner);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginPartner(@RequestBody @Valid UserLoginDTO userLoginDTO) {

        return super.login(userLoginDTO);
    }

    @Override
    public BaseAuthenticationService<Partner> getAuthenticationService() {
        return partnerAuthenticationServiceImpl;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getNewUserRefreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {

        return super.getNewRefreshToken(refreshTokenDTO);
    }

    // metodo para mudar a senha que recebe um cnpj e confira se o mesmo é valido,
        // se valido, envia um email contendo um PIM
        // se inserido um pim valido
            // mostra a tela de mudanca de senha com 2 campos: nova senha e confirmacao de nova senha
}
