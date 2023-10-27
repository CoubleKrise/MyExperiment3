package com.kk.iterate.test;

import org.junit.Test;

import com.kk.iterate.entity.ITMetapopulation;

/**
 * @author Krise
 * ������Դ��������
 */
public class TestMP_Sim_it {

	private ITMetapopulation i_mp;
	private String filePathTop = "folder/WSimplex2D_100_0.063_0.0001";
	private String filePathBot = "folder/WER_100_5.6";

	/**
	 * ��ͨ����
	 * 
	 */
	@Test
	public void test_with_time_it() {
		i_mp = new ITMetapopulation(filePathTop, filePathBot, /*N*/100, //
				/*��:������ʶ��Ϣ������*/0, /*��_0:0-��*/0.2, /*��_1:1-�� */0.1, /*��_2:2-�� */0.2, //
				/*----------------------- ��_0:0-�� */0.0, /*��_1:1-��*/0.0, /*��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.2, //
				/*��_U ������Ⱦ��*/0.02, /*��*/0.2, /*�� �����ָ�*/0.2, //
				/*p Ǩ�Ƹ���*/ 0.2, true);
		int timeStep = 0;
		while (true) {
			System.out.println(timeStep + "\t" + i_mp.statistics());
			i_mp.statistics();
			i_mp.moveInteractReturn();//��Ӧ����
			if (timeStep++ >= 100)
				break;
		} //

	}

	/****************************************************************
	 * ������ͬ�� betaU ���� �����ֵ...
	 * �� ������ʶ
	 * �� �����Ⱦ��
	 * 
	 */
	@Test
	public void test_threshold_it() {
		double step = 0.0001;
		//int count = 0;
		for (double betaU = 0.0000; betaU <= 0.0200; betaU = betaU + step) {
			i_mp = new ITMetapopulation(filePathTop, filePathBot, /*N*/100, //
					/*��:������ʶ��Ϣ������*/0.01, /*��_0:0-��*/0.2, /*��_1:1-�� */0.1, /*��_2:2-�� */0.2, //
					/*-----------------------  ��_0:0-�� */0, /*��_1:1-��*/1, /* ��_2:2-��*/0.0, /*�� ��ʶ�ָ���*/0.20, //
					/*��_U ������Ⱦ��*/betaU, /*��*/0.20, /*�� �����ָ�*/0.20, //
					/*p Ǩ�Ƹ���*/0.2, false);
			int timeStep = 0;
			while (true) {
				i_mp.moveInteractReturn();//��Ӧ����
				if (timeStep++ > 50)
					break;
			}
			System.out.println(betaU + "\t" + i_mp.statistics());
		} //for
	}

}// Class Test
