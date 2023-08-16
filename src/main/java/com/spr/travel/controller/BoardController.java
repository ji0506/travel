package com.spr.travel.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spr.travel.domain.Board;
import com.spr.travel.domain.Notice;
import com.spr.travel.domain.Qna;
import com.spr.travel.domain.User;
import com.spr.travel.service.BoardService;
import com.spr.travel.service.QnaService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	private BoardService bs;
	
	@Autowired
	private QnaService qs;

	@GetMapping("/faq.do")//자주 묻는 질문의 페이지 get 요청
	public String faq(Model model, HttpServletRequest request, HttpServletResponse response)throws Exception {
		List<Board> list = bs.getBoardList(); //게시판 목록을 가져옴
		model.addAttribute("boardlist", list); //가져온 게시판 목록을 boardlist 모델에 추가
		return "board/faq"; //자주 묻는 질문의 페이지를 반환해서 클라이언트에게 보여줌
	}
	
	@GetMapping("/faqWrite.do") //자주 묻는 질문 추가 페이지 get 요청
	public String faqWriteget(Model model, HttpServletRequest request, HttpServletResponse response)throws Exception {
		return "board/faqWrite"; // 질문 추가 페이지 반환
	}
	
	@PostMapping("/faqWrite.do") //자주 묻는 질문 추가 페이지 post 요청
	public String faqWritepost( @RequestParam("faq_category")int cateNo,
						 @RequestParam("faq_title")String title, @RequestParam("faq_content")String content) throws Exception {
		Board vo= new Board(title,content,cateNo); //전달받은 파라미터로부터 게시판 작성에 필요한 정보를 추출하여 Board 객체 생성
		bs.Write(vo);//게시판 데이터를 저장
		return "redirect:/board/faq.do"; //리다이렉트를 하여 게시판 목록을 보여줌
	}
	@GetMapping("/trip.do")
	public String trip( @RequestParam("menu")int menu, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//menu 파라미터를 받아와서 해당 카테고리의 게시글 목록을 가져옴
		List<Board> list = bs.getBoardCateList(menu);

		model.addAttribute("boardlist", list);// 게시글 목록을 모델에 추가하여 전달

		return "board/faq"; //자주 묻는 질문의 페이지 반환
	}

	@GetMapping("/qna.do")//qna 페이지 get 요청
	public String qna( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//Qna 게시판의 전체 게시글 목록을 가져옴
		List<Qna> list = qs.getBoardList();

		model.addAttribute("qnaList", list); //qna 게시글 목록을 모델에 추가하여 전달

		return "board/qna"; //qna 페이지로 반환 (이동)
	}

	@GetMapping("/qnaWrite.do")//글쓰기 페이지 get 요청
	public String qnaWriteget( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//qna 게시판에 글 쓰기 페이지로 이동
		return "board/qnaWrite";
	}
	@PostMapping("/qnaWrite.do") //글쓰기 페이지 post 요청
	public String qnaWritepost(@RequestParam("qna_title")String title,
						   @RequestParam("qna_question")String content, HttpServletRequest request) throws Exception {

		//세션에서 유저 정보 가져오기
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		//Qna 객체 생성 및 데이터 설정
		Qna vo= new Qna(title,content);
		vo.setUserId(user.getUserId());

		//Qna 게시글 작성
		qs.Write(vo);

		return "redirect:/board/qna.do";//qna 페이지로 리다이렉트
	}
	@GetMapping("/qnaDetail.do") //나의 질문 페이지 get 요청
	public String qnaDetail( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "board/qnaDetail";//나의 질문 페이지로 이동
	}
	
	@GetMapping("/notice.do") //공지사항 페이지 get 요청
	public String notice( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//게시판 카테고리 번호 6의 공지사항 목록을 가져옴
		List<Notice> list = bs.getNoticeList();

		//공지사항 목록을 모델에 추가하여 전달
		model.addAttribute("noticeList", list);

		return "board/notice"; // 공지사항 페이지로 이동
	}
	@GetMapping("/noticeWrite.do") //새글 작성 페이지 get 요청
	public String noticeWrite( Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "board/noticeWrite";// 새글 페이지로 이동
	}
	@PostMapping("/noticeWrite.do") // 새글 페이지 post 요청
	public String noticeWrite(@RequestParam("notice_title")String title,
						   @RequestParam("notice_content")String content,@RequestParam("notice_category")int cate,HttpServletRequest request) throws Exception {
		//세션에서 유저 정보를 가져옴
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		//notice 객체 생성, 데이터 설정
		Notice vo= new Notice(title,content,cate);
		vo.setUserId(user.getUserId());

		//공지사항 게시글 작성
		bs.Write(vo);

		return "redirect:/board/notice.do"; //공지사항 페이지로 리다이렉트		
	}

	
	@GetMapping("/noticeDetail.do") //새글 작성 페이지 get 요청
	public String noticeDetail( Model model, @RequestParam("notiNo") int notiNo) throws Exception {
	
		Notice list = bs.getByNoticeId(notiNo);

		//공지사항 목록을 모델에 추가하여 전달
		model.addAttribute("info", list);
		
		return "board/noticeDetail";// 새글 페이지로 이동
	}
	
	
	@GetMapping("/boardDetail.do") //새글 작성 페이지 get 요청
	public String boardDetail( Model model, @RequestParam("brdNo") int brdNo) throws Exception {
	
		Board list = bs.getByBoardId(brdNo);

		//공지사항 목록을 모델에 추가하여 전달
		model.addAttribute("info", list);
		
		return "board/noticeDetail";// 새글 페이지로 이동
	}
	
	
	
}
