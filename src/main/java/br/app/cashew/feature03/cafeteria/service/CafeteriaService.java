package br.app.cashew.feature03.cafeteria.service;

import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.partner.Owner;
import br.app.cashew.feature01.authentication.model.partner.Partner;
import br.app.cashew.feature01.authentication.repository.PartnerRepository;
import br.app.cashew.feature03.cafeteria.exception.CafeteriaDoesNotExistException;
import br.app.cashew.feature03.cafeteria.exception.campus.CampusDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.cnpj.CnpjAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.cnpj.CnpjInvalidException;
import br.app.cashew.feature03.cafeteria.model.Cafeteria;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.Category;
import br.app.cashew.feature03.cafeteria.model.product.Product;
import br.app.cashew.feature03.cafeteria.model.product.ProductStatus;
import br.app.cashew.feature03.cafeteria.repository.CafeteriaRepository;
import br.app.cashew.feature03.cafeteria.repository.CampusRepository;
import br.app.cashew.feature03.cafeteria.repository.CategoryRepository;
import br.app.cashew.feature03.cafeteria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class CafeteriaService {

    private final CafeteriaRepository cafeteriaRepository;
    private final PartnerRepository partnerRepository;
    private final CampusRepository campusRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CafeteriaService(
            CafeteriaRepository cafeteriaRepository,
            PartnerRepository partnerRepository,
            CampusRepository campusRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.cafeteriaRepository = cafeteriaRepository;
        this.partnerRepository = partnerRepository;
        this.campusRepository = campusRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    public Cafeteria createCafeteria(Cafeteria cafeteria, String partnerPublicKey) {
        validateCnpj(cafeteria.getCnpj());
        boolean isCampusValid = validateCampus(cafeteria.getCampus().getCampusID());
        if (!(isCampusValid)) {
            throw new CampusDoesNotExistsException("Campus nao existe", "campus");
        }
        setOwner(cafeteria, partnerPublicKey);
        cafeteria.setPublicKey(UUID.randomUUID());
        cafeteriaRepository.save(cafeteria);
        return cafeteria;
    }

    public Cafeteria getCafeteria(String cafeteriaPublicKey) {

        UUID uuid = UUID.fromString(cafeteriaPublicKey);

        return cafeteriaRepository.findByPublicKey(uuid)
                .orElseThrow(() -> new CafeteriaDoesNotExistException("Cafeteria nao existe"));
    }

    private void setOwner(Cafeteria cafeteria, String partnerPublicKey) {
        Partner partner = partnerRepository.findByPartnerPublicKey(UUID.fromString(partnerPublicKey))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario invalido, usuario nao existe"));
        Owner owner = partner.getOwner();
        cafeteria.setOwner(owner);
    }

    private void validateCnpj(String cnpj) {

        if (!(isCnpjValid(cnpj))) {
            throw new CnpjInvalidException("CNPJ invalido", "cnpj");
        }

        if (!(isCnpjAvailable(cnpj))) {
            throw new CnpjAlreadyExistsException("CNPJ ja esta sendo utilizado", "cnpj");
        }
        // TODO consultar API do governo
    }

    private boolean isCnpjValid(String cnpj) {
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                cnpj.equals("88888888888888") || cnpj.equals("99999999999999"))
            return false;

        char dig13;
        char dig14;
        int sm;
        int i;
        int r;
        int num;
        int peso;

        try {
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                num = cnpj.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char)((11-r) + 48);

            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = cnpj.charAt(i)- 48;
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char)((11-r) + 48);

            return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
        } catch (InputMismatchException error) {
            return(false);
        }
    }

    private boolean isCnpjAvailable(String cnpj) {
        return !(cafeteriaRepository.existsByCnpj(cnpj));
    }

    private boolean validateCampus(int campusID) {

        Optional<Campus> campus = campusRepository.findById(campusID);

        return campus.isPresent();
    }

    public List<Product> getProductsFromCafeteria(UUID cafeteriaUuid, ProductStatus status) {
        Cafeteria cafeteria = cafeteriaRepository.findByPublicKey(cafeteriaUuid)
                .orElseThrow(() -> new CafeteriaDoesNotExistException("Lanchonete nao existe"));

        Function<Cafeteria, List<Product>> repositoryMethod = selectMethodForStatus(status);
        return repositoryMethod.apply(cafeteria);
    }

    public List<Category> getCategoriesFromCafeteria(UUID cafeteriaUuid) {
        Cafeteria cafeteria = cafeteriaRepository.findByPublicKey(cafeteriaUuid)
                .orElseThrow(() -> new CafeteriaDoesNotExistException("Lanchonete nao existe"));

        return categoryRepository.findByCafeteria(cafeteria);
    }

    private Function<Cafeteria, List<Product>> selectMethodForStatus(ProductStatus status) {
        EnumMap<ProductStatus, Function<Cafeteria, List<Product>>> statusToFunction = new EnumMap<>(ProductStatus.class);

        statusToFunction.put(ProductStatus.ALL, productRepository::findByCafeteria);
        statusToFunction.put(ProductStatus.ACTIVE, productRepository::findByCafeteriaAndStatusTrue);
        statusToFunction.put(ProductStatus.INACTIVE, productRepository::findByCafeteriaAndStatusFalse);

        return statusToFunction.get(status);
    }
}
