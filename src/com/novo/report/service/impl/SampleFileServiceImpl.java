package com.novo.report.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.novo.report.beans.*;
import com.novo.report.common.Result;
import com.novo.report.dao.one.SpecimenHeadDao;
import com.novo.report.dao.three.NewLimsSampleDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.HttpApiClientUtil;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SampleFileServiceImpl implements SampleFileService {

    @Autowired
    private SampleFileDao sampleFileDao;
    @Autowired
    private SpecimenHeadDao specimenHeadDao;
    @Autowired
    private NewLimsSampleDao newLimsSampleDao;
    private final Gson gson = new Gson();

    @Override
    public void addSampleFile(SampleFile sampleFile) {
		/*String specimen_type = sampleFile.getSpecimen_type();
		if(specimen_type!=null && specimen_type.contains("血") || "白细胞".equals(specimen_type) || "脑脊液".equals(specimen_type) || "骨髓".equals(specimen_type) || "胸腹水（上清）".equals(specimen_type)){
			sampleFile.setSample_type("blood");
		}else if(specimen_type!=null && specimen_type.contains("组织") || "石蜡卷片".equals(specimen_type) || "石蜡贴片".equals(specimen_type) || "贴片+卷片".equals(specimen_type) || "蜡块".equals(specimen_type) || "蜡块（对照）".equals(specimen_type) || "口腔拭子".equals(specimen_type) || "胸腹水".equals(specimen_type)){
			sampleFile.setSample_type("tissue");
		}*/
        sampleFileDao.insertSampleFile(sampleFile);
    }

    @Override
    public SampleFile querySampleFileBySubbarcode(String subbarcode) {
        return sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
    }

    @Override
    public Long getSampleIdBySubbarcode(String subbarcode) {
        return sampleFileDao.getSampleIdBySubbarcode(subbarcode);
    }

    @Override
    public PaginationVO<SampleFile> getsampleFileByPage(SampleFilePageBean sampleFilePageBean) {
        PaginationVO<SampleFile> paginationVO = new PaginationVO<SampleFile>();
        paginationVO.setTotal(sampleFileDao.getTotal(sampleFilePageBean));
        paginationVO.setDataList(sampleFileDao.getsampleFileByPage(sampleFilePageBean));
        return paginationVO;
    }

    @Override
    public void createSampleFile(SampleFile sampleFile) {
        sampleFileDao.insertSampleFile(sampleFile);
    }

    @Override
    public Integer getSampleIdByBarcode(String barcode) {
        return sampleFileDao.getSampleIdByBarcode(barcode);
    }

    @Override
    public Object RefulshLims() {
        try {
            List<SpecimenHead> shList = newLimsSampleDao.getNewLimsSampleList();
            for (SpecimenHead sh : shList) {
                SampleFile sf = new SampleFile();
                sh.setSubBarcode(sh.getBarcode());
                String age = sh.getAge();
                if (age == null || "".equals(age)) {
                    String birth = sh.getBirthday();
                    if (birth != null && !("".equals(birth))) {
                        long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth).getTime();
                        long currDate = new Date().getTime(); //
                        if (birthday <= currDate) { //
                            age = (((currDate - birthday) / (24 * 60 * 60 * 1000)) / 365) + "";
                        }
                    } else {
                        age = "";
                    }
                }
                if (age.length() < 2) {
                    age = "";
                }
                sf.setEmailaddress(sh.getEmailaddress());
                sf.setSaleremail(sh.getSaleremail());
                sf.setSupportemail(sh.getSupportemail());
                sf.setManageremail(sh.getManageremail());
                sf.setPmemail(sh.getPmemail());
                sf.setPatient_id(sh.getIdnum());
                sf.setProduct_name(sh.getErptestname() == null ? sh.getErptestname() : sh.getErptestname().trim());
                sf.setCancertype(sh.getCancertype());
                sf.setPathologicaltype(sh.getPathologicaltype());
                sf.setAge(age);
                sf.setClient(sh.getPatientname());
                sf.setSales_contact(sh.getErpsalername());
                sf.setHospital(sh.getCustomername() == null ? "" : sh.getCustomername());
                sf.setCommission_date(sh.getEnterdate() == null ? "" : sh.getEnterdate().substring(0, 10));
                sf.setReceived_date(sh.getGetspecdate() == null ? "" : sh.getGetspecdate().substring(0, 10));
                sf.setPerson_name(sh.getPatientname());
                sf.setGender(sh.getSex());
                sf.setBarcode(sh.getBarcode());
                sf.setSubbarcode(sh.getSubBarcode());
                sf.setBirthday(sh.getBirthday() == null ? "" : sh.getBirthday().substring(0, 10));
                if (sh.getClinicalremark() == null || "".equals(sh.getClinicalremark())) {
                    sf.setDisease_type(sh.getCancertype());
                    sf.setClinicalremark(sh.getCancertype());
                } else {
                    sf.setDisease_type(sh.getClinicalremark());
                }
                sf.setReport_receiver(sh.getReportreceiver());
                sf.setSpecimen_type(sh.getSampletype() == null ? (sh.getShsampletype() == null ? (sh.getSrsampletype() == null ? sh.getSrsampletype() : sh.getSrsampletype().trim()) : sh.getShsampletype().trim()) : sh.getSampletype());
                String specimen_type = sf.getSpecimen_type();
                if (specimen_type != null && specimen_type.contains("血")) {
                    sf.setSample_type("blood");
                } else if (specimen_type != null && specimen_type.contains("组织")) {
                    sf.setSample_type("tissue");
                }
                String specimennum = sh.getSpecimennum();
                String samplenum = sh.getSamplenum();
                String unit = sh.getUnit();
                String sampleunit = sh.getSampleunit();
                sf.setSpecimen_quantity(((specimennum == null ? samplenum : specimennum) == null ? "" : (specimennum == null ? samplenum : specimennum)) + ((unit == null ? sampleunit : unit) == null ? "" : (unit == null ? sampleunit : unit)));
                sf.setCollect_date(sh.getSenddate() == null ? "" : sh.getSenddate().substring(0, 10));
                sf.setClinicalstages(sh.getClinicalstages());
                sf.setDoctorname(sh.getDoctorname());
                sf.setFastcode(sh.getFastcode());
                sf.setLibraryname(sh.getLibraryname());
                sf.setLocationname(sh.getLocationname());
                sf.setReport_upload_date(sh.getOperatedate());
                sf.setSample_source(sh.getSamplesource());
                sf.setFrom_organ(sh.getFromorgan());
                sf.setGene_type(sh.getGenetype());
                sf.setGene_result(sh.getGeneresult());
                sf.setBirthplace(sh.getBirthplace());
                sf.setFamily_history(sh.getFamilyhistory());
                sampleFileDao.insertSampleFile(sf);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateSmapleType(SampleFile sampleFile) {
        sampleFileDao.updateSmapleType(sampleFile);
    }

    @Override
    public Result<JsonObject> updatePersonName(SampleFile sampleFile) {
        try {
            sampleFileDao.updatePersonName(sampleFile);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(500, "数据库更新失败：" + e.getMessage());
        }

        String subbarcode = sampleFile.getSubbarcode();
        String personName = sampleFile.getPerson_name();

        String url = String.format(
                "http://qrcode.novogene.com/index.php/Api/Reportid/qrcodeup/subbarcode/%s/client/%s",
                subbarcode, personName
        );

        // 修改二维码信息
        JsonObject res = HttpApiClientUtil.sendGet(url, null, null);

        try {

            int status = res.has("status") ? res.get("status").getAsInt() : -1;
            String info = res.has("info") ? res.get("info").getAsString() : "无消息";
            if (status == 1){
                return Result.success("更新姓名成功，二维码信息" + info ,null);
            }else {
                return Result.failure(500, "更新姓名失败，请重试。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(500, "更新姓名失败，请重试。");
        }
    }

    @Override
    public void updateGender(SampleFile sampleFile) {
        sampleFileDao.updateGender(sampleFile);
    }

    @Override
    public void updateAge(SampleFile sampleFile) {
        sampleFileDao.updateAge(sampleFile);
    }

    @Override
    public void updateDiseaseType(SampleFile sampleFile) {
        sampleFileDao.updateDiseaseType(sampleFile);
    }

    @Override
    public void updateSpecimenno(SampleFile sampleFile) {
        sampleFileDao.updateSpecimenno(sampleFile);
    }

    @Override
    public Integer isExistPerson_id(Integer person_id) {

        return sampleFileDao.isExistPerson_id(person_id);
    }

    @Override
    public SampleFile getSampleFileBySubbarcode(String subbarcode) {
        return sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
    }

    @Override
    public void saveMutationsNum(NumberOfMutations numberOfMutations) {
        sampleFileDao.saveMutationsNum(numberOfMutations);
    }

    @Override
    public void saveMutationsNum2(Integer mut_num, String subbarcode, String analysis_date, String file_type) {
        sampleFileDao.saveMutationsNum2(mut_num, subbarcode, analysis_date, file_type);
    }

    @Override
    public List<Map> findMutationsNum(String subbarcode, String analysis_date) {
        return sampleFileDao.findMutationsNum(subbarcode, analysis_date);
    }

    @Override
    public void exportPcrFile(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception {
        String filepath = session.getServletContext().getRealPath("/");
        String filename = "样本模板.xls";

        //获得请求头中的User-Agent
        String agent = request.getHeader("User-Agent");
        //根据不同浏览器进行不同的编码
        String filenameEncoder = "";
        if (agent.contains("MSIE") || agent.contains("Trident")) {
            // IE浏览器
            filenameEncoder = URLEncoder.encode(filename, "utf-8");
            filenameEncoder = filenameEncoder.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
        } else {
            // 其它浏览器
            filenameEncoder = URLEncoder.encode(filename, "utf-8");
        }

        //要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
        response.setContentType(request.getServletContext().getMimeType(filename));
        //告诉客户端该文件不是直接解析 而是以附件形式打开(下载)
        response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
        //根据路径读取文件
        InputStream in = new FileInputStream(filepath + "/templates/" + filename);
        //将文件写入到response缓冲区
        response.getOutputStream();
        //获得输出流---通过response获得的输出流 用于向客户端写内容
        ServletOutputStream out = response.getOutputStream();
        //下载
        IOUtils.copy(in, out);
        //关流
        in.close();
    }

    @Transactional
    @Override
    public void execute_inputSampleFile(MultipartFile filename) throws Exception {
        if (!filename.isEmpty()) {
            InputStream in = filename.getInputStream();
            HSSFWorkbook hssfworkbook = new HSSFWorkbook(in);
            for (int SheetNum = 0; SheetNum < hssfworkbook.getNumberOfSheets(); SheetNum++) {
                //获取excel表格中的sheet页
                HSSFSheet hssfsheet = hssfworkbook.getSheetAt(SheetNum);
                if (hssfsheet == null) {
                    continue;
                } else {
                    for (int rowNum = 1; rowNum <= hssfsheet.getLastRowNum(); rowNum++) {
                        //创建一个行对象。
                        HSSFRow hssfrow = hssfsheet.getRow(rowNum);
                        //实体对象
                        SampleFile sampleFile = new SampleFile();
                        if (hssfrow == null) {
                            continue;
                        } else {
                            //设置数据格式


                            hssfrow.getCell(0).setCellType(CellType.STRING);
                            hssfrow.getCell(1).setCellType(CellType.STRING);
                            hssfrow.getCell(2).setCellType(CellType.STRING);
                            hssfrow.getCell(3).setCellType(CellType.STRING);
                            hssfrow.getCell(4).setCellType(CellType.STRING);
                            hssfrow.getCell(5).setCellType(CellType.STRING);
                            hssfrow.getCell(6).setCellType(CellType.STRING);
                            hssfrow.getCell(7).setCellType(CellType.STRING);
                            hssfrow.getCell(8).setCellType(CellType.STRING);
                            hssfrow.getCell(9).setCellType(CellType.STRING);
                            hssfrow.getCell(10).setCellType(CellType.STRING);
                            hssfrow.getCell(11).setCellType(CellType.STRING);
                            hssfrow.getCell(12).setCellType(CellType.STRING);
                            hssfrow.getCell(13).setCellType(CellType.STRING);
                            hssfrow.getCell(14).setCellType(CellType.STRING);
                            hssfrow.getCell(15).setCellType(CellType.STRING);
                            hssfrow.getCell(16).setCellType(CellType.STRING);
                            hssfrow.getCell(17).setCellType(CellType.STRING);
                            hssfrow.getCell(18).setCellType(CellType.STRING);
                            hssfrow.getCell(19).setCellType(CellType.STRING);
                            hssfrow.getCell(20).setCellType(CellType.STRING);
                            hssfrow.getCell(21).setCellType(CellType.STRING);
                            hssfrow.getCell(22).setCellType(CellType.STRING);
                            hssfrow.getCell(23).setCellType(CellType.STRING);
                            hssfrow.getCell(24).setCellType(CellType.STRING);
                            hssfrow.getCell(25).setCellType(CellType.STRING);
                            hssfrow.getCell(26).setCellType(CellType.STRING);
                            hssfrow.getCell(27).setCellType(CellType.STRING);
                            hssfrow.getCell(28).setCellType(CellType.STRING);
                            hssfrow.getCell(29).setCellType(CellType.STRING);
                            hssfrow.getCell(30).setCellType(CellType.STRING);
                            hssfrow.getCell(31).setCellType(CellType.STRING);
                            hssfrow.getCell(32).setCellType(CellType.STRING);
                            hssfrow.getCell(33).setCellType(CellType.STRING);
                            hssfrow.getCell(34).setCellType(CellType.STRING);
                            hssfrow.getCell(35).setCellType(CellType.STRING);
                            hssfrow.getCell(36).setCellType(CellType.STRING);
                            hssfrow.getCell(37).setCellType(CellType.STRING);
                            hssfrow.getCell(38).setCellType(CellType.STRING);
                            hssfrow.getCell(39).setCellType(CellType.STRING);
                            hssfrow.getCell(40).setCellType(CellType.STRING);
                            hssfrow.getCell(41).setCellType(CellType.STRING);
                            hssfrow.getCell(42).setCellType(CellType.STRING);
                            hssfrow.getCell(43).setCellType(CellType.STRING);
                            hssfrow.getCell(44).setCellType(CellType.STRING);
                            hssfrow.getCell(45).setCellType(CellType.STRING);
                            hssfrow.getCell(46).setCellType(CellType.STRING);
                            hssfrow.getCell(47).setCellType(CellType.STRING);
                            hssfrow.getCell(48).setCellType(CellType.STRING);
                            hssfrow.getCell(49).setCellType(CellType.STRING);
                            hssfrow.getCell(50).setCellType(CellType.STRING);
                            hssfrow.getCell(51).setCellType(CellType.STRING);
                            hssfrow.getCell(52).setCellType(CellType.STRING);
                            hssfrow.getCell(53).setCellType(CellType.STRING);
                            hssfrow.getCell(54).setCellType(CellType.STRING);
                            hssfrow.getCell(55).setCellType(CellType.STRING);
                            hssfrow.getCell(56).setCellType(CellType.STRING);
                            hssfrow.getCell(57).setCellType(CellType.STRING);
                            hssfrow.getCell(58).setCellType(CellType.STRING);
                            hssfrow.getCell(59).setCellType(CellType.STRING);

                            /**/
                            String subbarcode = hssfrow.getCell((short) 0).getStringCellValue();
                            sampleFile.setSubbarcode(subbarcode);

                            String person_name = "";
                            if (!(hssfrow.getCell((short) 1) == null)) {
                                person_name = hssfrow.getCell((short) 1).getStringCellValue().trim();
                            }
                            if (!"".equals(person_name)) {
                                sampleFile.setPerson_name(person_name);
                                sampleFile.setClient(person_name);
                            }

                            String barcode = "";
                            if (!(hssfrow.getCell((short) 2) == null)) {
                                barcode = hssfrow.getCell((short) 2).getStringCellValue().trim();
                            }
                            if (!"".equals(barcode)) {
                                sampleFile.setBarcode(barcode);
                            }

                            String gender = "";
                            if (!(hssfrow.getCell((short) 3) == null)) {
                                gender = hssfrow.getCell((short) 3).getStringCellValue().trim();
                            }
                            if (!"".equals(gender)) {
                                sampleFile.setGender(gender);
                            }

                            String specimen_type = "";
                            if (!(hssfrow.getCell((short) 4) == null)) {
                                specimen_type = hssfrow.getCell((short) 4).getStringCellValue().trim();
                            }
                            if (!"".equals(specimen_type)) {
                                sampleFile.setSpecimen_type(specimen_type);
                            }

                            String age = "";
                            if (!(hssfrow.getCell((short) 5) == null)) {
                                age = hssfrow.getCell((short) 5).getStringCellValue().trim();
                            }
                            if (!"".equals(age)) {
                                //DecimalFormat format = new DecimalFormat("#");
                                //sampleFile.setAge(format.format(age));
                                sampleFile.setAge(age);
                            }

                            String commission_date = "";
                            if (!(hssfrow.getCell((short) 6) == null)) {
                                commission_date = hssfrow.getCell((short) 6).getStringCellValue().trim();
                            }
                            if (!"".equals(commission_date)) {
                                sampleFile.setCommission_date(commission_date);
                            }

                            String run_name = "";
                            if (!(hssfrow.getCell((short) 7) == null)) {
                                run_name = hssfrow.getCell((short) 7).getStringCellValue().trim();
                            }
                            if (!"".equals(run_name)) {
                                sampleFile.setRun_name(run_name);
                            }

                            String run_code = "";
                            if (!(hssfrow.getCell((short) 8) == null)) {
                                run_code = hssfrow.getCell((short) 8).getStringCellValue().trim();
                            }
                            if (!"".equals(run_code)) {
                                sampleFile.setRun_code(run_code);
                            }

                            String dna_index = "";
                            if (!(hssfrow.getCell((short) 9) == null)) {
                                dna_index = hssfrow.getCell((short) 9).getStringCellValue().trim();
                            }
                            if (!"".equals(dna_index)) {
                                sampleFile.setDna_index(dna_index);
                            }

                            String rna_index = "";
                            if (!(hssfrow.getCell((short) 10) == null)) {
                                rna_index = hssfrow.getCell((short) 10).getStringCellValue().trim();
                            }
                            if (!"".equals(rna_index)) {
                                sampleFile.setRna_index(rna_index);
                            }

                            String template_subbarcode = "";
                            if (!(hssfrow.getCell((short) 11) == null)) {
                                template_subbarcode = hssfrow.getCell((short) 11).getStringCellValue().trim();
                            }
                            if (!"".equals(template_subbarcode)) {
                                sampleFile.setTemplate_subbarcode(template_subbarcode);
                            }

                            String DNAQubit = "";
                            if (!(hssfrow.getCell((short) 12) == null)) {
                                DNAQubit = hssfrow.getCell((short) 12).getStringCellValue().trim();
                            }
                            if (!"".equals(DNAQubit)) {
                                sampleFile.setDNAQubit(DNAQubit);
                            }

                            String RNAQubit = "";
                            if (!(hssfrow.getCell((short) 13) == null)) {
                                RNAQubit = hssfrow.getCell((short) 13).getStringCellValue().trim();
                            }
                            if (!"".equals(RNAQubit)) {
                                sampleFile.setRNAQubit(RNAQubit);
                            }

                            String disease_type = "";
                            if (!(hssfrow.getCell((short) 14) == null)) {
                                disease_type = hssfrow.getCell((short) 14).getStringCellValue().trim();
                            }
                            if (!"".equals(disease_type)) {
                                sampleFile.setDisease_type(disease_type);
                                sampleFile.setPathologicaltype(disease_type);
                            }

                            String room = "";
                            if (!(hssfrow.getCell((short) 15) == null)) {
                                room = hssfrow.getCell((short) 15).getStringCellValue().trim();
                            }
                            if (!"".equals(room)) {
                                sampleFile.setRoom(room);
                            }

                            String locationname = "";
                            if (!(hssfrow.getCell((short) 16) == null)) {
                                locationname = hssfrow.getCell((short) 16).getStringCellValue().trim();
                            }
                            if (!"".equals(locationname)) {
                                sampleFile.setLocationname(locationname);
                            }

                            String ward = "";
                            if (!(hssfrow.getCell((short) 17) == null)) {
                                ward = hssfrow.getCell((short) 17).getStringCellValue().trim();
                            }
                            if (!"".equals(ward)) {
                                sampleFile.setWard(ward);
                            }

                            String DNANucleic = "";
                            if (!(hssfrow.getCell((short) 18) == null)) {
                                DNANucleic = hssfrow.getCell((short) 18).getStringCellValue().trim();
                            }
                            if (!"".equals(DNANucleic)) {
                                sampleFile.setDNANucleic(DNANucleic);
                            }

                            String RNANucleic = "";
                            if (!(hssfrow.getCell((short) 19) == null)) {
                                RNANucleic = hssfrow.getCell((short) 19).getStringCellValue().trim();
                            }
                            if (!"".equals(RNANucleic)) {
                                sampleFile.setRNANucleic(RNANucleic);
                            }

                            String DNALibrary = "";
                            if (!(hssfrow.getCell((short) 20) == null)) {
                                DNALibrary = hssfrow.getCell((short) 20).getStringCellValue().trim();
                            }
                            if (!"".equals(DNALibrary)) {
                                String[] split = DNALibrary.split("\\.");
                                if (DNALibrary.indexOf(".") != -1 && split[1].length() >= 2) {
                                    sampleFile.setDNALibrary(String.format("%.2f", Double.parseDouble(DNALibrary)));
                                } else {
                                    sampleFile.setDNALibrary(DNALibrary);
                                }
                            }

                            String RNALibrary = "";
                            if (!(hssfrow.getCell((short) 21) == null)) {
                                RNALibrary = hssfrow.getCell((short) 21).getStringCellValue().trim();
                            }
                            if (!"".equals(RNALibrary)) {
                                String[] split = RNALibrary.split("\\.");
                                if (RNALibrary.indexOf(".") != -1 && split[1].length() >= 2) {
                                    sampleFile.setRNALibrary(String.format("%.2f", Double.parseDouble(RNALibrary)));
                                } else {
                                    sampleFile.setRNALibrary(RNALibrary);
                                }
                            }

                            String DNAPlaneData = "";
                            if (!(hssfrow.getCell((short) 22) == null)) {
                                DNAPlaneData = hssfrow.getCell((short) 22).getStringCellValue().trim();
                            }
                            if (!"".equals(DNAPlaneData)) {
                                sampleFile.setDNAPlaneData(DNAPlaneData);
                            }

                            String meanSequencingDepth = "";
                            if (!(hssfrow.getCell((short) 23) == null)) {
                                meanSequencingDepth = hssfrow.getCell((short) 23).getStringCellValue().trim();
                            }
                            if (!"".equals(meanSequencingDepth)) {
                                sampleFile.setMeanSequencingDepth(meanSequencingDepth);
                            }

                            String targetAreaCoverage = "";
                            if (!(hssfrow.getCell((short) 24) == null)) {
                                targetAreaCoverage = hssfrow.getCell((short) 24).getStringCellValue().trim();
                            }
                            if (!"".equals(targetAreaCoverage)) {
                                sampleFile.setTargetAreaCoverage(targetAreaCoverage);
                            }

                            String RNAPlaneData = "";
                            if (!(hssfrow.getCell((short) 25) == null)) {
                                RNAPlaneData = hssfrow.getCell((short) 25).getStringCellValue().trim();
                            }
                            if (!"".equals(RNAPlaneData)) {
                                sampleFile.setRNAPlaneData(RNAPlaneData);
                            }

                            String ReadsNumber = "";
                            if (!(hssfrow.getCell((short) 26) == null)) {
                                ReadsNumber = hssfrow.getCell((short) 26).getStringCellValue().trim();
                            }
                            if (!"".equals(ReadsNumber)) {
                                sampleFile.setReadsNumber(ReadsNumber);
                            }

                            String doctorname = "";
                            if (!(hssfrow.getCell((short) 27) == null)) {
                                doctorname = hssfrow.getCell((short) 27).getStringCellValue().trim();
                            }
                            if (!"".equals(doctorname)) {
                                sampleFile.setDoctorname(doctorname);
                            }

                            String clinicaldiagnosis = "";
                            if (!(hssfrow.getCell((short) 28) == null)) {
                                clinicaldiagnosis = hssfrow.getCell((short) 28).getStringCellValue().trim();
                            }
                            if (!"".equals(clinicaldiagnosis)) {
                                sampleFile.setClinicaldiagnosis(clinicaldiagnosis);
                            }

                            String bed = "";
                            if (!(hssfrow.getCell((short) 29) == null)) {
                                bed = hssfrow.getCell((short) 29).getStringCellValue().trim();
                            }
                            if (!"".equals(bed)) {
                                sampleFile.setBed(bed);
                            }

                            String consultation = "";
                            if (!(hssfrow.getCell((short) 30) == null)) {
                                consultation = hssfrow.getCell((short) 30).getStringCellValue().trim();
                            }
                            if (!"".equals(consultation)) {
                                sampleFile.setConsultation(consultation);
                            }

                            String cancertype = "";
                            if (!(hssfrow.getCell((short) 31) == null)) {
                                cancertype = hssfrow.getCell((short) 31).getStringCellValue().trim();
                            }
                            if (!"".equals(cancertype)) {
                                sampleFile.setCancertype(cancertype);
                            }

                            String birthday = "";
                            if (!(hssfrow.getCell((short) 32) == null)) {
                                birthday = hssfrow.getCell((short) 32).getStringCellValue().trim();
                            }
                            if (!"".equals(birthday)) {
                                sampleFile.setBirthday(birthday);
                            }

                            String patient_phone = "";
                            if (!(hssfrow.getCell((short) 33) == null)) {
                                patient_phone = hssfrow.getCell((short) 33).getStringCellValue().trim();
                            }
                            if (!"".equals(patient_phone)) {
                                sampleFile.setPatient_phone(patient_phone);
                            }

                            String patient_id = "";
                            if (!(hssfrow.getCell((short) 34) == null)) {
                                patient_id = hssfrow.getCell((short) 34).getStringCellValue().trim();
                            }
                            if (!"".equals(patient_id)) {
                                sampleFile.setPatient_id(patient_id);
                            }

                            String collect_date = "";
                            if (!(hssfrow.getCell((short) 35) == null)) {
                                collect_date = hssfrow.getCell((short) 35).getStringCellValue().trim();
                            }
                            if (!"".equals(collect_date)) {
                                sampleFile.setCollect_date(collect_date);
                            }

                            String sample_nature = "";
                            if (!(hssfrow.getCell((short) 36) == null)) {
                                sample_nature = hssfrow.getCell((short) 36).getStringCellValue().trim();
                            }
                            if (!"".equals(sample_nature)) {
                                sampleFile.setSample_nature(sample_nature);
                            }

                            String loaded_date = "";
                            if (!(hssfrow.getCell((short) 37) == null)) {
                                loaded_date = hssfrow.getCell((short) 37).getStringCellValue().trim();
                            }
                            if (!"".equals(loaded_date)) {
                                sampleFile.setLoaded_date(loaded_date);
                            }

                            String sample_source = "";
                            if (!(hssfrow.getCell((short) 38) == null)) {
                                sample_source = hssfrow.getCell((short) 38).getStringCellValue().trim();
                            }
                            if (!"".equals(sample_source)) {
                                sampleFile.setSample_source(sample_source);
                            }

                            String turmor_cell_ratio = "";
                            if (!(hssfrow.getCell((short) 39) == null)) {
                                turmor_cell_ratio = hssfrow.getCell((short) 39).getStringCellValue().trim();
                            }
                            if (!"".equals(turmor_cell_ratio)) {
                                sampleFile.setTurmor_cell_ratio(turmor_cell_ratio);
                            }

                            String sample_type = "";
                            if (!(hssfrow.getCell((short) 40) == null)) {
                                sample_type = hssfrow.getCell((short) 40).getStringCellValue().trim();
                            }
                            if (!"".equals(sample_type)) {
                                sampleFile.setSample_type(sample_type);
                            }

                            String tumorcellcontent = "";
                            if (!(hssfrow.getCell((short) 41) == null)) {
                                tumorcellcontent = hssfrow.getCell((short) 41).getStringCellValue().trim();
                            }
                            if (!"".equals(tumorcellcontent)) {
                                sampleFile.setTumorcellcontent(tumorcellcontent);
                            }

                            String DNA_total = "";
                            if (!(hssfrow.getCell((short) 42) == null)) {
                                DNA_total = hssfrow.getCell((short) 42).getStringCellValue().trim();
                            }
                            if (!"".equals(DNA_total)) {
                                sampleFile.setDNA_total(DNA_total);
                            }

                            String DNA_degradation = "";
                            if (!(hssfrow.getCell((short) 43) == null)) {
                                DNA_degradation = hssfrow.getCell((short) 43).getStringCellValue().trim();
                            }
                            if (!"".equals(DNA_degradation)) {
                                sampleFile.setDNA_degradation(DNA_degradation);
                            }

                            String Outbound_quantity = "";
                            if (!(hssfrow.getCell((short) 44) == null)) {
                                Outbound_quantity = hssfrow.getCell((short) 44).getStringCellValue().trim();
                            }
                            if (!"".equals(Outbound_quantity)) {
                                sampleFile.setOutbound_quantity(Outbound_quantity);
                            }

                            String plane_data = "";
                            if (!(hssfrow.getCell((short) 45) == null)) {
                                plane_data = hssfrow.getCell((short) 45).getStringCellValue().trim();
                            }
                            if (!"".equals(plane_data)) {
                                sampleFile.setPlane_data(plane_data);
                            }

                            String Sequencing_depth = "";
                            if (!(hssfrow.getCell((short) 46) == null)) {
                                Sequencing_depth = hssfrow.getCell((short) 46).getStringCellValue().trim();
                            }
                            if (!"".equals(Sequencing_depth)) {
                                sampleFile.setSequencing_depth(Sequencing_depth);
                            }

                            String coverage = "";
                            if (!(hssfrow.getCell((short) 47) == null)) {
                                coverage = hssfrow.getCell((short) 47).getStringCellValue().trim();
                            }
                            if (!"".equals(coverage)) {
                                sampleFile.setCoverage(coverage);
                            }

                            String coverage_uniformity = "";
                            if (!(hssfrow.getCell((short) 48) == null)) {
                                coverage_uniformity = hssfrow.getCell((short) 48).getStringCellValue().trim();
                            }
                            if (!"".equals(coverage_uniformity)) {
                                sampleFile.setCoverage_uniformity(coverage_uniformity);
                            }

                            String genome_alignment = "";
                            if (!(hssfrow.getCell((short) 49) == null)) {
                                genome_alignment = hssfrow.getCell((short) 49).getStringCellValue().trim();
                            }
                            if (!"".equals(genome_alignment)) {
                                sampleFile.setGenome_alignment(genome_alignment);
                            }

                            String base_quality = "";
                            if (!(hssfrow.getCell((short) 50) == null)) {
                                base_quality = hssfrow.getCell((short) 50).getStringCellValue().trim();
                            }
                            if (!"".equals(base_quality)) {
                                sampleFile.setBase_quality(base_quality);
                            }

                            String run_id = "";
                            if (!(hssfrow.getCell((short) 51) == null)) {
                                run_id = hssfrow.getCell((short) 51).getStringCellValue().trim();
                            }
                            if (!"".equals(run_id)) {
                                sampleFile.setRun_id(run_id);
                            }

                            String I5 = "";
                            if (!(hssfrow.getCell((short) 52) == null)) {
                                I5 = hssfrow.getCell((short) 52).getStringCellValue().trim();
                            }
                            if (!"".equals(I5)) {
                                sampleFile.setI5(I5);
                            }

                            String I7 = "";
                            if (!(hssfrow.getCell((short) 53) == null)) {
                                I7 = hssfrow.getCell((short) 53).getStringCellValue().trim();
                            }
                            if (!"".equals(I7)) {
                                sampleFile.setI7(I7);
                            }

                            String test_product = "";
                            if (!(hssfrow.getCell((short) 54) == null)) {
                                test_product = hssfrow.getCell((short) 54).getStringCellValue().trim();
                            }
                            if (!"".equals(test_product)) {
                                sampleFile.setTest_product(test_product);
                            }

                            String review_doctor = "";
                            if (!(hssfrow.getCell((short) 55) == null)) {
                                review_doctor = hssfrow.getCell((short) 55).getStringCellValue().trim();
                            }
                            if (!"".equals(review_doctor)) {
                                sampleFile.setReview_doctor(review_doctor);
                            }

                            String carcinoma = "";
                            if (!(hssfrow.getCell((short) 56) == null)) {
                                carcinoma = hssfrow.getCell((short) 56).getStringCellValue().trim();
                            }
                            if (!"".equals(carcinoma)) {
                                sampleFile.setCarcinoma(carcinoma);
                            }

                            String test_number = "";
                            if (!(hssfrow.getCell((short) 57) == null)) {
                                test_number = hssfrow.getCell((short) 57).getStringCellValue().trim();
                            }
                            if (!"".equals(test_number)) {
                                sampleFile.setTest_number(test_number);
                            }

                            String serial_number = "";
                            if (!(hssfrow.getCell((short) 58) == null)) {
                                serial_number = hssfrow.getCell((short) 58).getStringCellValue().trim();
                            }
                            if (!"".equals(serial_number)) {
                                sampleFile.setSerial_number(serial_number);
                            }

                            String registration_number = "";
                            if (!(hssfrow.getCell((short) 59) == null)) {
                                registration_number = hssfrow.getCell((short) 59).getStringCellValue().trim();
                            }
                            if (!"".equals(registration_number)) {
                                sampleFile.setRegistration_number(registration_number);
                            }

                            /**/
//report_upload_date,sample_source,pathology_number,from_organ


                            //String disease_type=hssfrow.getCell((short)14).getStringCellValue();
                            //sampleFile.setDisease_type(disease_type);
                            //String patient_id=hssfrow.getCell((short)15).getStringCellValue();
                            //sampleFile.setPatient_id(patient_id);
                            //String locationname=hssfrow.getCell((short)16).getStringCellValue();
                            //sampleFile.setLocationname(locationname);
                            //5个固定值
                            //sampleFile.setProduct_name("life");
                            sampleFile.setReport_type("标准版用药检测");
                            sampleFile.setDna_panel("novoPanelV3");
                            sampleFile.setRna_panel("OncomineFocusRNA");
                            //sampleFile.setPerson_id(sampleFile.getBarcode());
                            try {
                                createSampleFile(sampleFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException();
                            }
                        }
                    }
                }
            }
            in.close();
        }
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    // 获取错误邮箱
    @Override
    public ArrayList<String> getErrorEmail() {
        return sampleFileDao.getErrorEmail();
    }

    // 获取添加邮箱
    @Override
    public List<Map> getEmailByCustomer(String customer) {
        return sampleFileDao.getEmailByCustomer(customer);
    }

    // 获取添加邮箱
    @Override
    public List<Map> getEmailByRecordercode(String recordercode) {
        return sampleFileDao.getEmailByRecordercode(recordercode);
    }
}
