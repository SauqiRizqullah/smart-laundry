package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.dto.request.*;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.LoginResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.ServiceRepository;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.repository.TypeRepository;
import com.gruptiga.smartlaundry.service.AuthService;
import com.gruptiga.smartlaundry.service.JwtService;
import com.gruptiga.smartlaundry.service.ServiceServices;
import com.gruptiga.smartlaundry.validation.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountValidator accountValidator;

    @Autowired
    private final ServiceRepository serviceRepository;
    @Autowired
    private final TypeRepository typeRepository;
    @Autowired
    private final ServiceTypeRepository serviceTypeRepository;




    @PreAuthorize("permitAll()")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountResponse register(AccountRequest request) throws DataIntegrityViolationException {
        accountValidator.validateAccountRequest(request);

        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .email(request.getEmail())
                .password(hashPassword)
                .build();

        Account savedAccount = userAccountRepository.saveAndFlush(account);

        List<ServiceRequest> predefinedServices = Arrays.asList(
                new ServiceRequest(null, "Cuci Kering", request.getEmail()),
                new ServiceRequest(null, "Cuci Basah", request.getEmail()),
                new ServiceRequest(null, "Setrika", request.getEmail()),
                new ServiceRequest(null, "Cuci Sepatu", request.getEmail()),
                new ServiceRequest(null, "Cuci Jas", request.getEmail()),
                new ServiceRequest(null, "Cuci Selimut", request.getEmail()),
                new ServiceRequest(null, "Cuci Sprei", request.getEmail()),
                new ServiceRequest(null, "Cuci Karpet", request.getEmail())
        );

        List<TypeRequest> predefinedTypes = Arrays.asList(
                new TypeRequest(null, "REGULAR", request.getEmail()),
                new TypeRequest(null, "EXPRESS", request.getEmail()),
                new TypeRequest(null, "ONEDAY", request.getEmail())
        );

        List<com.gruptiga.smartlaundry.entity.Service> savedServices = new ArrayList<>();
        List<Type> savedTypes = new ArrayList<>();

        // Hardcoded prices for each service-type combination (24 combinations)
        Long[] hardcodedPrices = new Long[] {
                8000L, 10000L, 12000L, 5000L, 7000L, 10000L, 6000L, 8000L,
                10000L, 30000L, 40000L, 15000L, 12000L, 25000L, 190000L, 200000L,
                210000L, 220000L, 230000L, 240000L, 250000L, 260000L, 270000L, 280000L
        };

        Map<String, String> imagePathMap = new HashMap<>();
        imagePathMap.put("Cuci Kering", "ik.imagekit.io/px1nwaaeg/test/cucikering_F_n_nHUbo.png");
        imagePathMap.put("Cuci Basah", "path/to/image2.jpg");
        imagePathMap.put("Setrika", "ik.imagekit.io/px1nwaaeg/test/setrika_uRi7uFlyH.png");
        imagePathMap.put("Cuci Sepatu", "path/to/image4.jpg");
        imagePathMap.put("Cuci Jas", "ik.imagekit.io/px1nwaaeg/test/jas_Z945yOC6n.png");
        imagePathMap.put("Cuci Selimut", "path/to/image6.jpg");
        imagePathMap.put("Cuci Sprei", "ik.imagekit.io/px1nwaaeg/test/sprei_CArESkbnT.png");
        imagePathMap.put("Cuci Karpet", "path/to/image8.jpg");

        // Save services and collect them
        for (ServiceRequest serviceRequest : predefinedServices) {
            com.gruptiga.smartlaundry.entity.Service service = com.gruptiga.smartlaundry.entity.Service.builder()
                    .name(serviceRequest.getName())
                    .account(savedAccount) // Attach the saved Account entity
                    .build();

            savedServices.add(serviceRepository.save(service));
        }

        // Save types and collect them
        for (TypeRequest typeRequest : predefinedTypes) {
            Type type = com.gruptiga.smartlaundry.entity.Type.builder()
                    .name(typeRequest.getName())
                    .account(savedAccount)
                    .build();

            savedTypes.add(typeRepository.save(type));
        }

        List<ServiceTypeRequestAccount> serviceTypeRequests = new ArrayList<>();
        int priceIndex = 0;
        for (com.gruptiga.smartlaundry.entity.Service savedService : savedServices) {
            String imagePath = imagePathMap.get(savedService.getName());
            for (Type savedType : savedTypes) {
                String detail = "SATUAN";
                Long price = hardcodedPrices[priceIndex++];

                serviceTypeRequests.add(new ServiceTypeRequestAccount(
                        null,
                        request.getEmail(),
                        savedType.getTypeId(),
                        savedService.getServiceId(),
                        price,
                        detail,
                        imagePath // Tambahkan field ini
                ));
            }
        }

        for (ServiceTypeRequestAccount serviceTypeRequest : serviceTypeRequests) {
            ServiceType serviceType = ServiceType.builder()
                    .service(savedServices.stream().filter(s -> s.getServiceId().equals(serviceTypeRequest.getServiceId())).findFirst().orElse(null))
                    .type(savedTypes.stream().filter(t -> t.getTypeId().equals(serviceTypeRequest.getTypeId())).findFirst().orElse(null))
                    .price(serviceTypeRequest.getPrice())
                    .detail(Detail.valueOf(serviceTypeRequest.getDetail()))
                    .account(savedAccount)
                    .imagePath(serviceTypeRequest.getImagePath()) // Tambahkan field ini
                    .build();

            serviceTypeRepository.save(serviceType);
        }

        return AccountResponse.builder()
                .accountId(savedAccount.getAccountId())
                .name(savedAccount.getName())
                .address(savedAccount.getAddress())
                .contact(savedAccount.getContact())
                .email(savedAccount.getEmail())
                .build();
    }





    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {

        // baru daftar aja
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        // nah ini pengecheck'an, berarti hasilnya data yg sudah valid
        Authentication authenticated = authenticationManager.authenticate(authentication);

        Account userAccount = (Account) authenticated.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .token(token)
                .email(userAccount.getEmail())
                .build();
    }
}
