package com.spr.travel.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spr.travel.domain.Product;
import com.spr.travel.domain.ProductDetail;
import com.spr.travel.domain.Reservation;
import com.spr.travel.domain.SearchForm;
import com.spr.travel.domain.User;
import com.spr.travel.service.DetailService;
import com.spr.travel.service.ProductService;
import com.spr.travel.service.ReserService;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
@RequestMapping("/products/*")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private DetailService detailService;
    @Autowired
    private ReserService reservationService;
 
	private String filePath = "C:/upload";
   
    @GetMapping("/index") // 전체 상품 보기
    public String index(Model model, HttpServletRequest request, HttpServletResponse response)throws Exception {

        List<Product> list = productService.getProductList();

        model.addAttribute("continent", "모든 상품 정보");
        listSplitAndAdd(model, list);

        return "products/index";
    }

    @GetMapping("/search")
    public String productSearch(Model model, SearchForm search) {
        if (search.getCountry() == "" && search.getDeparture() == "" && search.getPlan() == "" && search.getSeat() == "" && search.getCity() == "" ) {
            return "redirect:/products/index";
        }
        //List<Product> list = productService.productSearch(search);
        
       // model.addAttribute("list", list);

        //listSplitAndAdd(model, list);
        return "/products/index";
    }

    @GetMapping("/{id}") // 상세 페이지
    public String renderDetail(@PathVariable("id") int id, HttpServletRequest req, HttpServletResponse res,Product product, Model model) {
        /*productService.updateViewcnt(req, res, id,product);*/
        ProductDetail detail = detailService.getDetailById(id); // DB에 등록된 상세 테이블에서 id와 같은 값을 가진 id의 정보를 가져온다.
        Product product1 = productService.getProductById(id); // DB에 등록된 상품 테이블에서 id와 같은 값을 가진 id의 정보를 가져온다.

        if (detail == null) return "products/index"; // 상세 정보가 없으면 목록창으로 이동
        model.addAttribute("detail", detail); // 상세 테이블의 데이터를 출력
        model.addAttribute("product", product1);  // 상품 테이블의 데이터 출력

        return "products/detail";
    }

	@PostMapping("/{id}")
	public String reservation(HttpSession session, @PathVariable int id, Reservation rvo, RedirectAttributes rttr) {
		User loginMember = (User) session.getAttribute("user"); // 세션 정보 읽기
		rvo.setProNo(id);     //상품 id 세팅
		int result = productService.reserve(rvo);   // 예약정보 저장
        return "redirect:/member/myPage"; // 예약 정보 페이지로
	}
	
    
    @GetMapping("/new") // 새글 작성 관리자만 사용 가능
    public String renderNewForm() {

        return "products/new";
    }

    @PostMapping("/index") // new 페이지에서 이동됨
    public String postForm(Product product,ProductDetail detail ,
    		@RequestParam("proDepartureStr") String proDeparture,
    		@RequestParam("proArriveStr") String proArrive,
    		@RequestParam("uploadFile") MultipartFile[] uploadFile) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 날짜데이터 형식 불러오기
        product.setProDeparture(formatter.parse(proDeparture)); // String 타입인 proDeparture를 Date 타입으로 변환
        product.setProArrive(formatter.parse(proArrive)); // String 타입인 proArrive를 Date 타입으로 변환

        productService.saveListByNew(product); // 입력된 데이터를 상품 테이블에 저장
        detail.setProNo(product.getProNo()); // 상품 테이블에서 id를 찾아 상세 테이블에 세팅하기
        detailService.saveListByNew(detail); // 입력된 데이터와 세팅된 id를 상세 테이블에 저장
        fileUpload(detail,uploadFile);	// 파일 저장
        
        return "redirect:/products/" + product.getProNo();
    }


    @GetMapping("/continent/{continent}") // 대륙별 목록 리스트
    public String indexByContinent(@PathVariable String continent, Model model) {
        List<Product> list = productService.getListByContinent(continent); // DB에 등록된 대륙별 리스트에서 특정 대륙 데이터를 가져온다
        model.addAttribute("continent", continent); // 특정 대륙 데이터를 입력된 "continent"롤 정의 하여 검색 한다.
        model.addAttribute("list", list); // 특정 대륙 데이터를 입력된 "list"롤 정의 하여 검색 한다.
        listSplitAndAdd(model, list); // 리스트 출력
        return "/products/index";
    }

    @GetMapping("/city/{city}") // 도시별 목록 리스트
    @ResponseBody
    public List<Product> getListByCity(@PathVariable String city, Model model) {
        List<Product> list = productService.getListByCity(city);
        return list;
    }

    @GetMapping("/{id}/reservation") // 예약 페이지
    public String renderReservationForm(@PathVariable int id, Model model, HttpSession session, RedirectAttributes rttr) {
        User loginMember = (User) session.getAttribute("user"); // 세션 정보 읽기
        if (loginMember != null) {  //이미 예약시 예외처리
            if (reservationService.getReservationOfMember(loginMember.getUserId(), id) != null) {
                rttr.addFlashAttribute("flashMessage", "이미 예약하신 상품입니다.");
                return "redirect:/products/" + id;
            }
        }
        model.addAttribute("product", productService.getProductById(id));   // product 정보 세팅
        return "/products/reserve";
    }

    @GetMapping("/{id}/update") // 수정 페이지 관리자만 사용 가능
    public String renderUpdateForm(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.getProductById(id)); // 상품 id로 데이터를 찾기
        model.addAttribute("product_detail", detailService.updateDetailByProNo(id)); // 상품 id에 저장된 상세정보 수정
        return "products/update";
    }

    @GetMapping("/{id}/delete") // 삭제 페이지 관리자만 사용 가능
    public String deleteProduct(@PathVariable int id, RedirectAttributes rttr) throws Exception {
        String redirectUrl = "redirect:/products/"; // 삭제 완료 후 경로

        detailService.deleteFileById(id);
        int result = productService.deleteProduct(id);
        if (result > 0) {
            rttr.addFlashAttribute("flashMessage", "정상적으로 삭제가 완료되었습니다.");
            return "redirect:/main/main.do";
        } else {
            rttr.addFlashAttribute("flashMessage", "삭제 중 오류가 발생하였습니다.");
            return redirectUrl + id;
        }
    }
    
	private String fileUpload(ProductDetail detail, MultipartFile[] uploadFile) {
		String uploadFolder = filePath + "/site";

		System.out.println("상품 번호: ");
		String uploadFolderPath = String.format("%d", detail.getProNo());

		// make folder ---------
		File uploadPath = new File(uploadFolder, uploadFolderPath); // 상품 번호로 경로를 만듦

		if (uploadPath.exists() == false) { // 해당 상품 폴더가 없으면
			uploadPath.mkdirs(); // 해당 경로에 폴더를 만든다.
		}

		for (MultipartFile multipartFile : uploadFile) {
			String uploadFileName = multipartFile.getOriginalFilename();

			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);

			UUID uuid = UUID.randomUUID(); // 첨부파일은 randomUUID를 이용해서 임의의 값을 생성할 수 있다.

			uploadFileName = uuid.toString() + "_" + uploadFileName; // 생성된 값은 원래의 파일 이름과 구분할 수 있도록 중간에 '_'를 추가할 수 있음

			// File saveFile = new File(uploadFolder, uploadFileName);

			try {

				File saveFile = new File(uploadPath, uploadFileName); // c:upload/main/{상품 번호 폴더}/파일 이름으로 최종 경로 생성
				multipartFile.transferTo(saveFile); // 파일을 최종 경로로 이동

				detail.setDetailImage("/" + uploadFileName);
				
				detailService.updateFilePath(detail);
				// 만일 이미지 타입이라면 섬네일을 생성하도록 한다.
				// check image type file
				if (checkImageType(saveFile)) {
					// FileOutputStream :데이터를 파일에 바이트 스트림으로 저장
					// File 클래스는 파일과 디렉터리를 다룸. 그래서 File 인스턴스는 파일일 수도 있고 디렉터리 일수도 있다.
					// File(String parent, String child) - parent 폴더 경로의 child라는 파일에 대한 File 객체 생성
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} // end catch
		} // end for
		
		return null;
	}
	
	private boolean checkImageType(File file) {

		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
    
    
    private void listSplitAndAdd(Model model, List<Product> list) {
        Set<String> countrySet = new HashSet<>(); // DB에 등록된 모든 country 데이터 수집
        list.stream().forEach(p -> countrySet.add(p.getProCountry())); // 수집된 데이트롤 이용하여 countrySet을 리스트화

        Map<String, Set<String>> cityMap = new HashMap<>(); // DB에 등록된 모든 city 데이터 수집
        countrySet.stream().forEach(country -> {
            Set<String> citySet = new HashSet<>(); // DB에 등록된 country 별로 city 데이터를 분류하고 citySet에 저장

            list.stream().filter(p -> p.getProCountry().equals(country)).forEach(p -> citySet.add(p.getProCity()));
            cityMap.put(country, citySet); // cityMa에 country를 키값으로 설정한 후 해당 country의 city목록을 값으로 입력
        });
        if (model.getAttribute("continent") ==null) model.addAttribute("continent", "검색 결과");
        model.addAttribute("countrySet", countrySet);
        model.addAttribute("cityMap", cityMap);
    }

}

