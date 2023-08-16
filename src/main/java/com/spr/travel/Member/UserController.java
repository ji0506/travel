package com.spr.travel.Member;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.spr.travel.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spr.travel.domain.Product;
import com.spr.travel.domain.Reservation;
import com.spr.travel.domain.User;
import com.spr.travel.service.ProductService;
import com.spr.travel.service.ReserService;

@Controller
@RequestMapping("/member/*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ReserService reserService;

	@Autowired
	private ProductService productService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailService emailService;


	@RequestMapping("/main.do")
	public String list() {
		return "member/login2";
	}

	// (회원)마이페이지 출력-예약내역
	@GetMapping("/myPage")
	@Transactional
	public String myPage(User user,HttpServletRequest request,HttpSession session,Model model) throws Exception{
		session = request.getSession();
		user = (User) session.getAttribute("user");
			
		List<Reservation> rev = reserService.getReserList(user);
//		List<Product> pList = productService.findProductList(rev);
			
			
		model.addAttribute("list", rev);
			
		
		return "member/info";
	}
	
	@RequestMapping("/logout.do")
	public String logout(ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
		HttpSession session = request.getSession();

        
		session.removeAttribute("user");

		return "redirect:/main/main.do";
	}

	@RequestMapping("/join.do")
	public String join(ModelMap model,HttpServletRequest request, HttpServletResponse response) {


		return "/member/join";
	}

	@RequestMapping("/save.do")
	public String save(User user) {

		user.setUserPwd(sha256Hash(user.getUserPwd()));// 비밀번호 암호화
		userService.createUser(user);		//저장

		return "redirect:/member/main.do";
	}
	
	// 관리자 페이지 이동
	@GetMapping("/adminPage")
	public String adminPage(Model model,HttpServletRequest request) throws Exception{
		String type = request.getParameter("type");
		System.out.println(type);
	/*	if(type != null) {
			if(type.equals("회원")) {
				String member_master = "N";
				List<Reservatio> rev = res.allRev();
				List<MemberVO> list = ms.typeMemberList(cri,member_master);
				PageMaker pm = ms.typePageMaker(cri,member_master);
				
				model.addAttribute("rev",rev);
				model.addAttribute("list",list);
				model.addAttribute("pm",pm);
				return "admin/index";
			}else {
				String member_master = "Y";
				List<User> list = ms.typeMemberList();
//				PageMaker pm = ms.typePageMaker(cri,member_master);
				
				model.addAttribute("list",list);
				model.addAttribute("pm",pm);
				return "admin/index";
			}
		}*/
		List<User> list = userService.memberList();
		List<Reservation> rev = userService.allRev();
		
//		PageMaker pm  = ms.pageMaker(cri);
		
		model.addAttribute("rev",rev);
		model.addAttribute("list",list);
//		model.addAttribute("pm",pm);
		return "admin/index";
	}
	
	@GetMapping("/reservDetail")
	public String reservDetail(Product vo,HttpSession session,Model model)throws Exception{
		User loginMember = (User) session.getAttribute("user");				// 유저 정보 세팅
		Reservation reserv = reserService.getReservationOfMember(loginMember.getUserId(),vo.getProNo()); //예약정보 불러오기

		Product product = productService.getProductById(vo.getProNo());	//상품 정보 불러오기
		
		model.addAttribute("product",product);	//상품 정보 세팅
		model.addAttribute("reservation",reserv);	// 예약정보 세팅
		model.addAttribute("tripInfo",product.getDetail()); // 상품 상세정보 세팅
		
		return "member/product";
	}
	
	
	@RequestMapping("/checkId.do")
	@ResponseBody
	public int idCheck(ModelMap model,HttpServletRequest request, @RequestParam("userId") String userID){
		int result = -1;
		if(userService.getUserById(userID) !=null) {		// 아이디 체크
			result = 1;
		}else {
			result = 0;
		}
		
		return result;
	}
	
	// 아이디 찾기 페이지이동
	@GetMapping("id_find")
	public String id_find(Model model) {
		String id = "아이디찾기";
		model.addAttribute("id",id);
		return "member/find";
	}
	
	// 비밀번호 찾기 페이지이동
	@GetMapping("pw_find")
	public String pw_find() {
		return "member/find";
	}
	

	// 계정정보 찾기 시도
	@PostMapping("findInfo")
	@ResponseBody
	public User findId(User vo) {
		User findMember = userService.getUserByEmail(vo.getUserEmail());
		
		return findMember;

	}
	

	//인증메일 전송
	@GetMapping("/checkEmail")
	@ResponseBody
	public String sendMail(@RequestParam("member_email") String email)throws Exception {
		System.out.println(email);
		String code = "";
		for(int i=0; i<5;i++) {
			code +=(int)(Math.random()*10);			// 5자리 랜덤 코드 생성
		}
		try {

			emailService.sendSimpleMessage(email,"인증 메일입니다.","인증 코드 : <h3>["+code+"]</h3>"); // 메일 발송
			System.out.println("발신 완료");
		}catch (Exception e) {
			e.printStackTrace();
		}

		return code;
	}

	

	public String sha256Hash(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			// 해시 값을 16진수 문자열로 변환
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// (회원) 정보수정 페이지이동
	@GetMapping("/edit_check")
	public String changeInfo() {
		return "member/editcheck";
	}


}
