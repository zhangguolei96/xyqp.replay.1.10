package com.zhuoan.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensitivewordFilter {


	@SuppressWarnings("rawtypes")
	public static  Map sensitiveWordMap = null;
	public static int minMatchTYpe = 1;      
	public static int maxMatchType = 2;     
	
	
	public SensitivewordFilter(){
		sensitiveWordMap = initKeyWord();
	}
	
	@SuppressWarnings("rawtypes")
	public static Map initKeyWord(){
		try {
			
			Set<String> keyWordSet = readSensitiveWordFile();
			
			addSensitiveWordToHashMap(keyWordSet);
			//spring��ȡapplication��Ȼ��application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     
		String key = null;  
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		//���keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //�ؼ���
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //ת����char��
				Object wordMap = nowMap.get(keyChar);       //��ȡ
				
				if(wordMap != null){        //�����ڸ�key��ֱ�Ӹ�ֵ
					nowMap = (Map) wordMap;
				}
				else{     
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //�������һ��
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				
				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //���һ��
				}
			}
		}
	}

	
	@SuppressWarnings("resource")
	public static Set<String> readSensitiveWordFile() throws Exception{
		Set<String> set = null;
		
		File file = new File("C:\\SensitiveWord.txt");    //��ȡ�ļ�
		InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
		try {
			if(file.isFile() && file.exists()){      //�ļ����Ƿ����
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while((txt = bufferedReader.readLine()) != null){    
					set.add(txt);
			    }
			}
			else{         
				throw new Exception("���дʿ��ļ�������");
			}
		} catch (Exception e) {
			throw e;
		}finally{
			read.close();     //�ر��ļ���
		}
		return set;
	}
	
	public boolean isContaintSensitiveWord(String txt,int matchType){
		boolean flag = false;
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = SensitivewordFilter.CheckSensitiveWord(txt, i, matchType); //�ж��Ƿ�������ַ�
			if(matchFlag > 0){    //����0���ڣ�����true
				flag = true;
			}
		}
		return flag;
	}
	
	
	public static Set<String> getSensitiveWord(String txt , int matchType){
		Set<String> sensitiveWordList = new HashSet<String>();
		
		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt, i, matchType);    //�ж��Ƿ�������ַ�
			if(length > 0){   
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //��1��ԭ������Ϊfor������
			}
		}
		
		return sensitiveWordList;
	}
	
	
	public static String replaceSensitiveWord(String txt,int matchType,String replaceChar){
		String resultTxt = txt;
		Set<String> set = getSensitiveWord(txt, matchType);     //��ȡ���е����д�
		Iterator<String> iterator = set.iterator();
		String word = null;
		String replaceString = null;
		while (iterator.hasNext()) {
			word = iterator.next();
			replaceString = getReplaceChars(replaceChar, word.length());
			resultTxt = resultTxt.replaceAll(word, replaceString);
		}
		
		return resultTxt;
	}
	
	
	private static String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
		
		return resultReplace;
	}
	

	@SuppressWarnings({ "rawtypes"})
	public static int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    
		int matchFlag = 0;     
		char word = 0;
		Map nowMap = initKeyWord();
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     
			if(nowMap != null){    
				matchFlag++;     
				if("1".equals(nowMap.get("isEnd"))){      
					flag = true;      
					if(SensitivewordFilter.minMatchTYpe == matchType){    
						break;
					}
				}
			}
			else{     
				break;
			}
		}
		if(matchFlag < 2 || !flag){         
			matchFlag = 0;
		}
		return matchFlag;
	}
	
	public static void main(String[] args) {
		 SensitivewordFilter filter = new SensitivewordFilter();  
	        System.out.println("敏感词的数量：" + filter.sensitiveWordMap.size());  
	        String string = "习近平、李克强、张德江、俞正声、太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"  
	                        + "然后法.轮.功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"  
	                        + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三.级.片 深人静的晚上，关上电话静静的发呆着。";  
	        System.out.println("待检测语句字数：" + string.length());  
	        long beginTime = System.currentTimeMillis();  
	        Set<String> set = filter.getSensitiveWord(string, 1);  
	        long endTime = System.currentTimeMillis();  
	        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);  
	        System.out.println("总共消耗时间为：" + (endTime - beginTime)); 
	        String str=filter.replaceSensitiveWord(string, 1, "*");
	        System.out.println("替换后：" + str); 
	}

}
