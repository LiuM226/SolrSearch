//package com.jxtii.solr.util;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
///**
// * Created by guolf on 17/7/3.
// */
//public final class ResponseUtil {
//    private ResponseUtil() {
//    }
//
//    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
//        return wrapOrNotFound(maybeResponse, (HttpHeaders)null);
//    }
//
//    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
//        return (ResponseEntity)maybeResponse.map((response) -> {
//            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(header)).body(response);
//        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
//    }
//}
