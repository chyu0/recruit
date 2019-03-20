package com.cy.recruit.controller;

import com.cy.recruit.base.modal.ResultInfo;

public class BaseController {

    public ResultInfo success(){
        return ResultInfo.newInstance().setCode(200).setMessage("success");
    }

    public ResultInfo fail(){
        return ResultInfo.newInstance().setCode(500).setMessage("fail");
    }
}
