package com.github.nut077.lotto;

import com.github.nut077.lotto.entity.Period;
import com.github.nut077.lotto.repository.PeriodRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
@EnableEncryptableProperties
@RequiredArgsConstructor
public class LottoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LottoApplication.class, args);
	}

	private final PeriodRepository periodRepository;

	@Override
	public void run(String... args) throws Exception {
		Period period = Period.builder()
			.id(55L)
			.periodDate(LocalDate.now(ZoneId.of("Asia/Bangkok")))
			.build();
		periodRepository.save(period);
	}
}
