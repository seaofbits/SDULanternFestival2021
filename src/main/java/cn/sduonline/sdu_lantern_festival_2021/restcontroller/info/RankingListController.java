package cn.sduonline.sdu_lantern_festival_2021.restcontroller.info;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Ranking;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.service.info.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class RankingListController {
    @Autowired
    RankingService rankingService;

    @Auth
    @GetMapping(path = "/info/ranking_list")
    public Response getRankingList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                   @RequestParam(value = "limit", defaultValue = "10") int limit) {
        if (offset < 0) {
            return Response.fail(ResponseCode.ERROR_PARAMETER_INVALID_VALUE, "offset < 0");
        }
        if (limit < 0) {
            return Response.fail(ResponseCode.ERROR_PARAMETER_INVALID_VALUE, "limit < 0");
        }
        List<Ranking> rankingList = rankingService.getCachedRankingList(offset, limit);
        return Response.success(rankingList);
    }
}
