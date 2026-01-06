package sg.gov.moe.masking.service;

import java.io.File;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Service
public class S3WrapperService implements S3WrapperInterface {

	private static final Logger LOG = LoggerFactory.getLogger(S3WrapperService.class);

	@Value("${bucketName}")
    private String bucketName;

	@Value("${utilityPortal.bucketName}")
    private String utilityBucketName;

    private final AmazonS3 s3;

    private final AmazonS3 s3UtilityPortal;

	public S3WrapperService(AmazonS3 s3, AmazonS3 s3UtilityPortal) {
		this.s3 = s3;
		this.s3UtilityPortal = s3UtilityPortal;
	}

	@Override
	public PutObjectResult uploadFileToSD(String s3ZipFilename, File fileToUpload)throws SdkClientException, AmazonServiceException {
		return s3.putObject(bucketName, s3ZipFilename, fileToUpload);
	}

	@Override
	public PutObjectResult uploadFileToUtility(String s3ZipFilename, File fileToUpload)throws SdkClientException, AmazonServiceException {
		return s3UtilityPortal.putObject(utilityBucketName, s3ZipFilename, fileToUpload);
	}

	@Override
	public boolean deleteFile(String s3ZipFilename) {
		try {
			s3.deleteObject(bucketName, s3ZipFilename);
		}catch(Exception e) {
			LOG.error("Exception inside deleteFile() method: {}", ExceptionUtils.getStackTrace(e));
			return false;
		}
		return true;
	}

	/**
	 * @return File file object after downloading completes
	 */
	@Override
	public File downloadFile(String importFolder, String s3ZipFilename) {
		File tempFile = new File(importFolder.concat(s3ZipFilename));
		try {
			TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3).build();
			GetObjectRequest s3ObjectReq =  new GetObjectRequest(bucketName, s3ZipFilename);
			Download dwl = tm.download(s3ObjectReq, tempFile);
			dwl.waitForCompletion();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOG.error("Exception inside downloadFile() method: {}", ExceptionUtils.getStackTrace(e));
		}
		return tempFile;
	}

}