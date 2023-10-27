package com.kk.mimic.test;

import org.junit.*;

import com.kk.mimic.entity.Metapopulation;
import com.kk.utils.FileUtils;

/**
 * ������Դ����ĸ������
 * @author Krise
 * 
 */
public class TestMP_Sim {

	private Metapopulation mp;
	private String filePathTop = "folder/WSimplex2D_100_0.063_0.0001";
	private String filePathBot = "folder/WER_100_5.6";

	/**
	 * *
	 * ****************************************************************************************
	 * ��ͨ����    -timestep
	 * 
	 */
	@Test
	public void test_with_time_mimic() {
		mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
				/*��:������ʶ��Ϣ������*/0.00, /*��_0:0-��*/0.2, /*��_1:1-�� */0.1, /*��_2:2-�� */0.0, //
				/*----------------------- ��_0:0-�� */0.0, /*��_1:1-��*/0.0, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
				/*��_U ������Ⱦ��*/0.02, /*��*/0.2, /*�� �����ָ�*/0.2, //
				/*p Ǩ�Ƹ���*/0.2, true);
		int timeStep = 0;
		while (true) {
			System.out.println(timeStep + "\t" + mp.statistics());
			mp.statistics();
			mp.moveAndReactUnderBot();
			mp.moveAndReactUnderTop();
			if (timeStep++ >= 100)
				break;
		} //

	}//test1

	/*****************************************************************************************
	 * lambda ������ʶ
	 * betaU  �����Ⱦ��
	 */
	@Test
	public void testRes_threshold_mimic() {
		double step = 0.0001;
		//int count = 0;
		for (double betaU = 0.0000; betaU <= 0.0200; betaU = betaU + step) {
			mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
					/*��:������ʶ��Ϣ������*/0.01, /*��_0:0-��*/0.2, /*��_1:1-�� */0.1, /*��_2:2-�� */0.2, //
					/*-----------------------  ��_0:0-�� */0, /*��_1:1-��*/1, /* ��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.20, //
					/*��_U ������Ⱦ��*/betaU, /*��*/0.20, /*�� �����ָ�*/0.20, //
					/*p Ǩ�Ƹ���*/0.2, false);
			int timeStep = 0;
			while (true) {
				mp.moveAndReactUnderBot();//��������
				mp.moveAndReactUnderTop();//��ʶ����
				if (timeStep++ > 100)
					break;
			}
			System.out.println(betaU + "\t" + mp.statistics());
		} //for
	}
	
	
	/***************************************************************************************
	 ***************************************************************************************
	 * ��ά��ͼ
	 * �� - ��0
	 * 
	 */
	@Test
	public void headtmap_lambda_VS_theta0() {
		double delta_lambda = 0.001;
		double delta_theta0 = 0.01;
		StringBuilder sb = new StringBuilder();
		String filePath = "folder/lambda-theta0-HED";
		////0-0.4//0-0.02
		System.out.println("test starts................");
		for (double lambda = 0.0; lambda < 0.103; lambda += delta_lambda) {
			for (double theta0 = 0.0; theta0 < 1.04; theta0 += delta_theta0) {
				mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
						/*��:������ʶ��Ϣ������*/lambda, /*��_0:0-��*/0.2, /*��_1:1-�� */0.10, /*��_2:2-�� */0.2, //
						/*-----------------------   ��_0:0-��*/theta0, /*��_1:1-��*/0.0, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
						/*��_U ������Ⱦ��*/0.01, /*��*/0.2, /*�� �����ָ�*/0.2, //
						/*p Ǩ�Ƹ���*/0.2, false);
				int timeStep = 0;
				while (true) {
					mp.moveAndReactUnderBot();//��������
					mp.moveAndReactUnderTop();//��ʶ����
					if (timeStep++ > 70)
						break;
				} //while
				sb.append(lambda + "\t" + theta0 + "\t" + mp.statistics() + "\r\n");
				if (sb.length() > 1024 * 10) {
					FileUtils.append2File(filePath, sb.substring(0, sb.length()));
					sb.delete(0, sb.length());
				}
			} //for in
		} //for out
			//����ǰ�Ĳ���
		FileUtils.append2File(filePath, sb.substring(0, sb.length()));
		sb.delete(0, sb.length());
		System.out.println("test ends..................");
	}//test

	/***************************************************************************************
	 * ��ά��ͼ
	 * �� - ��1 
	 * 
	 */

	@Test
	public void headtmap_lambda_VS_theta1() {
		double delta_lambda = 0.001;
		double delta_theta1 = 0.01;
		StringBuilder sb = new StringBuilder();
		String filePath = "folder/lambda-theta1-HOD";
		////0-0.4//0-0.02
		System.out.println("test starts................");
		for (double lambda = 0.0; lambda < 0.103; lambda += delta_lambda) {
			for (double theta1 = 0.0; theta1 < 1.04; theta1 += delta_theta1) {
				mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
						/*��:������ʶ��Ϣ������*/lambda, /*��_0:0-��*/0.2, /*��_1:1-�� */0.10, /*��_2:2-�� */0.2, //
						/*-----------------------        ��_0:0-��*/0, /*��_1:1-��*/theta1, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
						/*��_U ������Ⱦ��*/0.01, /*��*/0.2, /*�� �����ָ�*/0.2, //
						/*p Ǩ�Ƹ���*/0.2, true);
				int timeStep = 0;
				while (true) {
					mp.moveAndReactUnderBot();//��������
					mp.moveAndReactUnderTop();//��ʶ����
					if (timeStep++ > 70)
						break;
				} //while
				sb.append(lambda + "\t" + theta1 + "\t" + mp.statistics() + "\r\n");
				if (sb.length() > 1024 * 10) {
					FileUtils.append2File(filePath, sb.substring(0, sb.length()));
					sb.delete(0, sb.length());
				}
			} //for in
		} //for out
			//����ǰ�Ĳ���
		FileUtils.append2File(filePath, sb.substring(0, sb.length()));
		sb.delete(0, sb.length());
		System.out.println("test ends..................");
	}//test


	/***************************************************************************************
	***************************************************************************************
	***************************************************************************************
	***************************************************************************************
	 * ��ά��ͼ
	 * �� - ��0 
	 * 
	 */
	@Test
	public void headtmap_beta_VS_theta0() {
		double delta_beta = 0.0002;
		double delta_theta0 = 0.01;
		StringBuilder sb = new StringBuilder();
		String filePath = "folder/beta-theta0-HOD";
		////0-0.4//0-0.02
		System.out.println("test starts......");
		for (double beta = 0.0; beta < 0.0204; beta += delta_beta) {
			for (double theta0 = 0.0; theta0 < 1.04; theta0 += delta_theta0) {
				mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
						/*��:������ʶ��Ϣ������*/0.01, /*��_0:0-��*/0.2, /*��_1:1-�� */0.1, /*��_2:2-�� */0.2, //
						/*-----------------------  ��_0:0-��*/theta0, /*��_1:1-��*/0.0, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
						/*��_U ������Ⱦ��*/beta, /*��*/0.2, /*�� �����ָ�*/0.2, //
						/*p Ǩ�Ƹ���*/0.2, true);
				int timeStep = 0;
				while (true) {
					mp.moveAndReactUnderBot();//��������
					mp.moveAndReactUnderTop();//��ʶ����
					if (timeStep++ > 70)
						break;
				} //while
				sb.append(beta + "\t" + theta0 + "\t" + mp.statistics() + "\r\n");
				if (sb.length() > 1024 * 10) {
					FileUtils.append2File(filePath, sb.substring(0, sb.length()));
					sb.delete(0, sb.length());
				}
			} //for in
		} //for out
			//����ǰ�Ĳ���
		FileUtils.append2File(filePath, sb.substring(0, sb.length()));
		sb.delete(0, sb.length());
		System.out.println("test ends......");

	}//test

	/***************************************************************************************
	 * ��ά��ͼ
	 * �� - ��1 
	 * 
	 */
	@Test
	public void headtmap_beta_VS_theta1() {
		double delta_beta = 0.0002;
		double delta_theta1 = 0.01;
		StringBuilder sb = new StringBuilder();
		String filePath = "folder/beta-theta1-HOD";
		////0-0.4//0-0.02
		System.out.println("test starts......");
		for (double beta = 0.0; beta < 0.0204; beta += delta_beta) {
			for (double theta1 = 0.0; theta1 < 1.04; theta1 += delta_theta1) {
				mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
						/*��:������ʶ��Ϣ������*/0.01, /*��_0:0-��*/0.2, /*��_1:1-�� */0.10, /*��_2:2-�� */0.2, //
						/*-----------------------  ��_0:0-�� */0.0, /*��_1:1-��*/theta1, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
						/*��_U ������Ⱦ��*/beta, /*��*/0.2, /*�� �����ָ�*/0.2, //
						/*p Ǩ�Ƹ���*/0.2, true);
				int timeStep = 0;
				while (true) {
					mp.moveAndReactUnderBot();//��������
					mp.moveAndReactUnderTop();//��ʶ����
					if (timeStep++ > 70)
						break;
				} //while
				sb.append(beta + "\t" + theta1 + "\t" + mp.statistics() + "\r\n");
				if (sb.length() > 1024 * 10) {
					FileUtils.append2File(filePath, sb.substring(0, sb.length()));
					sb.delete(0, sb.length());
				}
			} //for in
		} //for out
			//����ǰ�Ĳ���
		FileUtils.append2File(filePath, sb.substring(0, sb.length()));
		sb.delete(0, sb.length());
		System.out.println("test ends......");
	}//test

	
	/**************************************************************************************************
	**************************************************************************************************
	**************************************************************************************************
	* ��ά��ͼ
	 * ��0 - ��1 
	 * 
	 */
	@Test
	public void headtmap_theta0_VS_theta1() {
		double delta_theta0 = 0.01;
		double delta_theta1 = 0.01;
		StringBuilder sb = new StringBuilder();
		String filePath = "folder/theta0-theta1-HED";
		////0-0.4//0-0.02
		System.out.println("test starts................");
		for (double theta0 = 0.0; theta0 < 1.03; theta0 += delta_theta0) {
			for (double theta1 = 0.0; theta1 < 1.03; theta1 += delta_theta1) {
				mp = new Metapopulation(filePathTop, filePathBot, /*N*/100, //
						/*��:������ʶ��Ϣ������*/0.01, /*��_0:0-��*/0.2, /*��_1:1-�� */0.10, /*��_2:2-�� */0.2, //
						/*-----------------------        ��_0:0-��*/theta0, /*��_1:1-��*/theta1, /*��_2:2-��*/0, /*�� ��ʶ�ָ���*/0.2, //
						/*��_U ������Ⱦ��*/0.01, /*��*/0.2, /*�� �����ָ�*/0.2, //
						/*p Ǩ�Ƹ���*/0.2, false);
				int timeStep = 0;
				while (true) {
					mp.moveAndReactUnderBot();//��������
					mp.moveAndReactUnderTop();//��ʶ����
					if (timeStep++ > 70)
						break;
				} //while
				sb.append(theta0 + "\t" + theta1 + "\t" + mp.statistics() + "\r\n");
				if (sb.length() > 1024 * 10) {
					FileUtils.append2File(filePath, sb.substring(0, sb.length()));
					sb.delete(0, sb.length());
				}
			} //for in
		} //for out
			//����ǰ�Ĳ���
		FileUtils.append2File(filePath, sb.substring(0, sb.length()));
		sb.delete(0, sb.length());
		System.out.println("test ends..................");
	}//test

	//--------------------------------------------------------------------------------------------
}// Test
