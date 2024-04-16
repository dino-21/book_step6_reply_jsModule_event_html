package org.zerock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
//@AllArgsConstructor
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardservice;

	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list...............");
		List<BoardVO> list = boardservice.getList(cri);
		list.forEach(board -> log.info(board));
		model.addAttribute("list", list);
	//	model.addAttribute("pageMaker", new PageDTO(cri,123)); //레코드 전체갯수 123개, 13page
		int total = boardservice.getTotal(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total)); 
	}

	@PostMapping("/register")
	public String register(BoardVO vo, RedirectAttributes rttr) {
		log.info("register......." + vo);

		boardservice.register(vo);

		rttr.addFlashAttribute("result", vo.getBno());
		return "redirect:/board/list";
	}

	@GetMapping("/register")
	public void resister() {
			 
	}
	
	
	@GetMapping({"/get", "/modify"})
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {
		model.addAttribute("board", boardservice.get(bno));
	}

	
	
	@PostMapping("/modify")
	public String modify(BoardVO board,  @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify:" + board);
		
		
		if (boardservice.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/list";
	}

	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno,  @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("remove...." + bno);
		if (boardservice.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/list";
	}

	
}
