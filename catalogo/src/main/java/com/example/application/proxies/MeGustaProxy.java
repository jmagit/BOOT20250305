package com.example.application.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="MEGUSTA-SERVICE", url="http://localhost:8020")
public interface MeGustaProxy {
	@PostMapping(path = "/me-gusta/hash/{id}")
	String sendLike(@PathVariable int id);
	@PostMapping(path = "/me-gusta/hash/{id}")
	String sendLike(@PathVariable int id, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String authorization);
}
