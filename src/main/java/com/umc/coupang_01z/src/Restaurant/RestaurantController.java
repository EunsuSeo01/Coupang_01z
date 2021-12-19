package com.umc.coupang_01z.src.Restaurant;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.umc.coupang_01z.src.Restaurant.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.umc.coupang_01z.config.*;
import com.umc.coupang_01z.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/coupang-eats")
public class RestaurantController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final RestaurantService restaurantService;
    @Autowired
    private final JwtService jwtService; // JWT 사용 시

    public RestaurantController(RestaurantProvider restaurantProvider, RestaurantService restaurantService, JwtService jwtService) {
        this.restaurantProvider = restaurantProvider;
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
    }

    /*
     * 카테고리 조회 : [GET] /category
     */
    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<GetCategoryRes>> getCategory() throws BaseException {
        try {
            List<GetCategoryRes> getCategoryRes = restaurantProvider.getCategory();
            return new BaseResponse<>(getCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
     * 음식점 리스트 조회
     * 카테고리 구분 : [GET] /restaurant?categoryIdx
     * 1. default : [GET] /restaurant
     * 1. 별점 높은 순 : [GET] /restaurant?rate
     * 2. 치타 배달 : [GET] /restaurant?isCheetah
     * 3. 배달비 : [GET] /restaurant?deliveryFee
     * 4. 최소 주문비 : [GET] /restaurant?minOrderFee
     */
    @ResponseBody
    @GetMapping("/restaurant")
    public BaseResponse<GetRestListResponse> getRestaurantsInCategory(@RequestParam(required = false) String categoryIdx, @RequestParam(required = false) String rate, @RequestParam(required = false) String isCheetah, @RequestParam(required = false) String deliveryFee, @RequestParam(required = false) String minOrderFee) throws BaseException {
//    public BaseResponse<List<GetRestListRes>> getRestaurantsInCategory(@RequestParam(required = false) String categoryIdx, @RequestParam(required = false) String rate, @RequestParam(required = false) String isCheetah, @RequestParam(required = false) String deliveryFee, @RequestParam(required = false) String minOrderFee) throws BaseException {
        try {
            GetRestListResponse getRestListResponse = new GetRestListResponse();
            List<GetRestListRes> getRestListRes;

            if (categoryIdx != null) { // 카테고리 구분 시
                if (rate != null) { // 별점 높은 순
                    getRestListRes = restaurantProvider.getRestByRate(Integer.parseInt(categoryIdx));
                } else if (isCheetah != null) { // 치타 배달
                    getRestListRes = restaurantProvider.getRestByCheetah(Integer.parseInt(categoryIdx));
                } else if (deliveryFee != null) { // 배달비
                    getRestListRes = restaurantProvider.getRestByDeliveryFee(Integer.parseInt(categoryIdx), Integer.parseInt(deliveryFee));
                } else if (minOrderFee != null) { // 최소 주문비
                    getRestListRes = restaurantProvider.getRestByMinOrderFee(Integer.parseInt(categoryIdx), Integer.parseInt(minOrderFee));
                } else { // default
                    getRestListRes = restaurantProvider.getRest(Integer.parseInt(categoryIdx));
                }
            } else {
                if (rate != null) { // 별점 높은 순
                    getRestListRes = restaurantProvider.getRestByRate();
                } else if (isCheetah != null) { // 치타 배달
                    getRestListRes = restaurantProvider.getRestByCheetah();
                } else if (deliveryFee != null) { // 배달비
                    getRestListRes = restaurantProvider.getRestByDeliveryFee(Integer.parseInt(deliveryFee));
                } else if (minOrderFee != null) { // 최소 주문비
                    getRestListRes = restaurantProvider.getRestByMinOrderFee(Integer.parseInt(minOrderFee));
                } else { // default
                    getRestListRes = restaurantProvider.getRest();
                }
            }
            getRestListResponse.setRestaurantList(getRestListRes);
            return new BaseResponse<GetRestListResponse>(getRestListResponse);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
     * 음식점 조회 : [GET] /restaurant/:restIdx
     */
    @ResponseBody
    @GetMapping("/restaurant/{restIdx}")
    public BaseResponse<GetRestRes> getRestaurant(@PathVariable("restIdx") int restIdx) {
        try {
            return new BaseResponse<>(restaurantProvider.getRestaurant(restIdx));

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
     * 메뉴 조회 : [GET] /menu/:restIdx
     */
//    @ResponseBody
//    @GetMapping("/menu/{restIdx}")
//    public BaseResponse<GetRestRes> getMenu(@PathVariable("restIdx") int restIdx) {
//        try {
//            return new BaseResponse<>(restaurantProvider.getMenu(restIdx));
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }

}
