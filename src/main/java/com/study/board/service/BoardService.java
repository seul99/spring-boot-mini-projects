package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception {

        //저장할 경로 생성
        String projectpath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        //경로, 파일 이름 지정

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "-" + file.getOriginalFilename();
        File saveFile = new File(projectpath, fileName);

        // 파일 업로드 처리
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);

        // 게시글 저장
        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){

        return boardRepository.findAll(pageable); //DB에 있는 모든 정보 가져오기
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardRepository.findById(id).orElse(null);
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }

    // 글 수정 처리
    public void update(Board board, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            // 파일 처리 로직
            String projectpath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "-" + file.getOriginalFilename();
            File saveFile = new File(projectpath, fileName);
            try {
                file.transferTo(saveFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 게시글 업데이트
        boardRepository.save(board);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}

