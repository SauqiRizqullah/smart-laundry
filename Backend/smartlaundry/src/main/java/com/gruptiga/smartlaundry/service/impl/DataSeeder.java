package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
//import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.CustomerService;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final AccountService accountService;
    private final ServiceTypeService typeService;
    private final CustomerService customerService;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (accountService.count() == 0) {
            // Buat akun
            AccountRequest acc1 = new AccountRequest();
            acc1.setName("Nisa Laundry");
            acc1.setAddress("Jl. Komplek Timah Blok BB No. 18");
            acc1.setContact("082122290218");
            acc1.setEmail("nisalaundrydepok@gmail.com");
            acc1.setPassword(passwordEncoder.encode("nisalaundry"));

            AccountResponse acc1Response = accountService.createAccount(acc1);

            AccountRequest acc2 = new AccountRequest();
            acc2.setName("BannaWash Laundry");
            acc2.setAddress("Jl. H. Radin No.26");
            acc2.setContact("081210842266");
            acc2.setEmail("bannawashid@gmail.com");
            acc2.setPassword(passwordEncoder.encode("bannawash"));

            AccountResponse acc2Response = accountService.createAccount(acc2);

            log.info("Berhasil menginisiasi akun laundry di aplikasi ini!!!");
        } else {
            log.info("Akun laundry sudah tersedia di aplikasi ini!!!");
        }
//
//        if (typeService.count() == 0) {
//            // Ambil Account dari DB
//            Account acc1 = accountService.getByEmail("nisalaundrydepok@gmail.com");
//            Account acc2 = accountService.getByEmail("bannawashid@gmail.com");
//
//            // Buat ServiceType untuk acc1
//            ServiceTypeRequest type1 = new ServiceTypeRequest();
//            type1.setAccountId(acc1.getAccountId());
//            type1.setType(Type.REGULAR.toString());
//            type1.setService("Cuci Full");
//            type1.setPrice(8000L);
//            type1.setDetail(Detail.KILOAN.toString());
//
//            typeService.createServiceType(type1);
//
//            ServiceTypeRequest type2 = new ServiceTypeRequest();
//            type2.setAccountId(acc1.getAccountId());
//            type2.setType(Type.EXPRESS.toString());
//            type2.setService("Cuci Full");
//            type2.setPrice(11000L);
//            type2.setDetail(Detail.KILOAN.toString());
//
//            typeService.createServiceType(type2);
//
//            ServiceTypeRequest type3 = new ServiceTypeRequest();
//            type3.setAccountId(acc1.getAccountId());
//            type3.setType(Type.ONEDAY.toString());
//            type3.setService("Cuci Full");
//            type3.setPrice(15000L);
//            type3.setDetail(Detail.KILOAN.toString());
//
//            typeService.createServiceType(type3);
//
//            log.info("Berhasil menginisiasi data pelayanan laundry di aplikasi ini!!!");
//        } else {
//            log.info("Data pelayanan laundry sudah tersedia di aplikasi ini!!!");
//        }

//        if (customerService.count() == 0) {
//
//            Account acc1 = accountService.getByEmail("nisalaundrydepok@gmail.com");
//            Account acc2 = accountService.getByEmail("bannawashid@gmail.com");
//            // Buat Customer untuk acc1
//            CustomerRequest customer1 = new CustomerRequest();
//            customer1.setAccountId(acc1.getAccountId());
//            customer1.setName("Customer 1");
//            customer1.setAddress("Alamat Customer 1");
//            customer1.setPhoneNumber("081234567890");
//
//            customerService.createCustomer(customer1);
//
//            CustomerRequest customer2 = new CustomerRequest();
//            customer2.setAccountId(acc1.getAccountId());
//            customer2.setName("Customer 2");
//            customer2.setAddress("Alamat Customer 2");
//            customer2.setPhoneNumber("081298765432");
//
//            customerService.createCustomer(customer2);
//
//            // Buat Customer untuk acc2
//            CustomerRequest customer3 = new CustomerRequest();
//            customer3.setAccountId(acc2.getAccountId());
//            customer3.setName("Customer 3");
//            customer3.setAddress("Alamat Customer 3");
//            customer3.setPhoneNumber("081212345678");
//
//            customerService.createCustomer(customer3);
//
//            log.info("Berhasil menginisiasi data customer di aplikasi ini!!!");
//        } else {
//            log.info("Data customer sudah tersedia di aplikasi ini!!!");
//        }
    }
}
