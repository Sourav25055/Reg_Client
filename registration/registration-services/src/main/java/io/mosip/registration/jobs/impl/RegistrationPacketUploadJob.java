package io.mosip.registration.jobs.impl;

import java.sql.Timestamp;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.util.DateUtils;
import io.mosip.registration.config.AppConfig;
import io.mosip.registration.constants.LoggerConstants;
import io.mosip.registration.constants.RegistrationConstants;
import io.mosip.registration.dto.ResponseDTO;
import io.mosip.registration.exception.RegBaseUncheckedException;
import io.mosip.registration.jobs.BaseJob;
import io.mosip.registration.service.packet.PacketUploadService;

/**
 * The {@code RegistrationPacketSyncJob} is a job to upload the synched packets
 * which extends {@code BaseJob}
 * 
 * <p>
 * This Job will be automatically triggered based on sync_frequency which has in
 * local DB.
 * </p>
 * 
 * <p>
 * If Sync_frequency = "0 0 11 * * ?" this job will be triggered everyday 11:00
 * AM, if it was missed on 11:00 AM, trigger on immediate application launch
 * </p>
 * 
 * @author Yaswanth S
 * @since 1.0.0
 *
 */
@DisallowConcurrentExecution
@Component(value = "registrationPacketUploadJob")
public class RegistrationPacketUploadJob extends BaseJob {

	/**
	 * The PacketUploadService
	 */

	@Autowired
	private PacketUploadService packetUploadService;

	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = AppConfig.getLogger(RegistrationPacketSyncJob.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.
	 * quartz.JobExecutionContext)
	 */
	@Async
	@Override
	public void executeInternal(JobExecutionContext context) {
		LOGGER.debug(LoggerConstants.REG_PACKET_SYNC_STATUS_JOB, RegistrationConstants.APPLICATION_NAME,
				RegistrationConstants.APPLICATION_ID, "job execute internal started");
		
		this.responseDTO = new ResponseDTO();
		try {
			this.jobId = loadContext(context);
			packetUploadService = applicationContext.getBean(PacketUploadService.class);

			// Execute Parent Job
			this.responseDTO = executeParentJob(jobId);
			
			// Execute Current Job
			if (responseDTO.getSuccessResponseDTO() != null) {
				this.responseDTO = packetUploadService.uploadSyncedPackets();
			}
			syncTransactionUpdate(responseDTO, triggerPoint, jobId, Timestamp.valueOf(DateUtils.getUTCCurrentDateTime()));
		} catch (RegBaseUncheckedException baseUncheckedException) {
			LOGGER.error(LoggerConstants.REG_PACKET_SYNC_STATUS_JOB, RegistrationConstants.APPLICATION_NAME,
					RegistrationConstants.APPLICATION_ID, baseUncheckedException.getMessage());
			throw baseUncheckedException;
		}

		LOGGER.debug(LoggerConstants.REG_PACKET_SYNC_STATUS_JOB, RegistrationConstants.APPLICATION_NAME,
				RegistrationConstants.APPLICATION_ID, "job execute internal Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.mosip.registration.jobs.BaseJob#executeJob(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ResponseDTO executeJob(String triggerPoint, String jobId) {
		LOGGER.debug(LoggerConstants.REG_PACKET_SYNC_STATUS_JOB, RegistrationConstants.APPLICATION_NAME,
				RegistrationConstants.APPLICATION_ID, "execute Job started");

		// Execute Parent Job
		this.responseDTO = executeParentJob(jobId);

		// Execute Current Job
		if (responseDTO.getSuccessResponseDTO() != null) {
			this.responseDTO = packetUploadService.uploadSyncedPackets();
		}
		syncTransactionUpdate(responseDTO, triggerPoint, jobId, Timestamp.valueOf(DateUtils.getUTCCurrentDateTime()));

		LOGGER.debug(LoggerConstants.REG_PACKET_SYNC_STATUS_JOB, RegistrationConstants.APPLICATION_NAME,
				RegistrationConstants.APPLICATION_ID, "execute job ended");

		return responseDTO;
	}

}
