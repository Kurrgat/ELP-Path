package com.example.emtechelppathbackend.hubs;

import com.example.emtechelppathbackend.chapter.Chapter;
import com.example.emtechelppathbackend.chapter.ChapterDTOUpdate;
import com.example.emtechelppathbackend.chapter.ChapterDto;
import com.example.emtechelppathbackend.exceptions.NoResourceFoundException;
import com.example.emtechelppathbackend.security.user.UsersDto;
import com.example.emtechelppathbackend.utils.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HubService {
    void createHub(Hub hub, MultipartFile file) throws IOException;
    CustomResponse<HubDTO> getHubById(Long hubId);
   CustomResponse<List<Hub>> getAllHubs();
    Hub joinHub(Long userId, Long hubId);
    Hub updateHubById(Long hubId, HubDTOUpdate updatedHubDto, MultipartFile hubImage) throws NoResourceFoundException, IOException;
    Hub leaveHub(Long userId, Long hubId);
    void deleteHub(Long hubId);
    CustomResponse<Set<UsersDto>> getMembersByHubId(Long hubId) throws NoResourceFoundException;

}
