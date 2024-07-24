package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final AccountService accountService;

    private final ServiceTypeService typeService;

    @PostConstruct
    public void init() {
        if (accountService.count() == 0) {


            // buat objek
            AccountRequest acc1 = new AccountRequest();
            acc1.setName("Nisa Laundry");
            acc1.setAddress("Jl. Komplek Timah Blok BB No. 18");
            acc1.setContact("082122290218");
            acc1.setEmail("nisalaundrydepok@gmail.com");
            acc1.setPassword("nisalaundry");

            accountService.createAccount(acc1);

            AccountRequest acc2 = new AccountRequest();
            acc2.setName("BannaWash Laundry");
            acc2.setAddress("Jl. H. Radin No.26");
            acc2.setContact("081210842266");
            acc2.setEmail("bannawashid@gmail.com");
            acc2.setPassword("bannawash");

            accountService.createAccount(acc2);

            log.info("Berhasil menginisiasi akun laundry di aplikasi ini!!!");
        } else {
            log.info("Akun laundry sudah tersedia di aplikasi ini!!!");
        }

        if (typeService.count() == 0){

            ServiceTypeRequest type1 = new ServiceTypeRequest();
            type1.setAccountId("A001");
            type1.setType(Type.REGULAR.toString());
            type1.setService("Cuci Full");
            type1.setPrice(parseLong("8000"));
            type1.setDetail(Detail.KILOAN.toString());

            typeService.createServiceType(type1);

            ServiceTypeRequest type2 = new ServiceTypeRequest();
            type2.setAccountId("A001");
            type2.setType(Type.EXPRESS.toString());
            type2.setService("Cuci Full");
            type2.setPrice(parseLong("11000"));
            type2.setDetail(Detail.KILOAN.toString());

            typeService.createServiceType(type2);

            ServiceTypeRequest type3 = new ServiceTypeRequest();
            type3.setAccountId("A001");
            type3.setType(Type.ONEDAY.toString());
            type3.setService("Cuci Full");
            type3.setPrice(parseLong("15000"));
            type3.setDetail(Detail.KILOAN.toString());

            typeService.createServiceType(type3);

            log.info("Berhasil menginisiasi data pelayanan laundry di aplikasi ini!!!");
        } else {
            log.info("Data pelayanan laundry sudah tersedia di aplikasi ini!!!");
        }
    }
}
