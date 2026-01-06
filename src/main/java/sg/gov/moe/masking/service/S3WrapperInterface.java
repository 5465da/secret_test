package sg.gov.moe.masking.service;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.PutObjectResult;

public interface S3WrapperInterface {

	PutObjectResult uploadFileToSD(String filename ,File fileToUpload) throws SdkClientException, AmazonServiceException ;

	PutObjectResult uploadFileToUtility(String filename ,File fileToUpload)throws SdkClientException, AmazonServiceException ;

	boolean deleteFile(String filename);

	File downloadFile(String importPath, String filename);

}