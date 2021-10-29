package com.team.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.team.project.item.Item;
import com.team.project.item.ItemTag;
import com.team.project.item.Material;
import com.team.project.item.Shape;
import com.team.project.member.Grade;
import com.team.project.member.Member;
import com.team.project.order.Order;
import com.team.project.order.OrderItemTag;
import com.team.project.order.OrderStatus;
import com.team.project.order.OrderTimeStamp;
import com.team.project.order.PersonCard;
import com.team.project.payment.BankbookPayInfo;
import com.team.project.payment.CardPayInfo;
import com.team.project.payment.PayInfo;
import com.team.project.payment.PayOption;

public class Application {

	public static ArrayList<Item> itemRepository;
	public static ArrayList<Member> memberRepository;
	public static ArrayList<Order> orderRepository;
	public static HashMap<String, ItemTag> itemTagRepository;
	public static Set<String> brandList;
	
	public static long loginMemberId;
	public static ArrayList<ItemTag> basket;
	
	public static Scanner scan;
	public static String sel;

	
	static {
		
		itemRepository = new ArrayList<Item>();
		memberRepository = new ArrayList<Member>();
		orderRepository = new ArrayList<Order>();
		itemTagRepository = new HashMap<String, ItemTag>();
		brandList = new HashSet<String>();
		
		loginMemberId = -1;
		basket = new ArrayList<ItemTag>();
		scan = new Scanner(System.in);
		sel = "";
		
		try {
			
			loadItem();
			loadMember();
			loadOrder();
			loadItemTag();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			System.out.println("[ERROR] 에러 꼭 찾고 넘어가세요.");
		}
	}
	
	
	public static void main(String[] args) {

		System.out.println("itemRepository = " + itemRepository.size());
		System.out.println("memberRepository = " + memberRepository.size());
		System.out.println("orderRepository = " + orderRepository.size());
		System.out.println("itemTagRepository = " + itemTagRepository.size());
		System.out.println("brandList = " + brandList.size());
		
		
		if (loginMemberId == -1) {
			//비회원 화면
			
			while (!sel.equals("q")) {
				itemListScreen();
			}
			
			
		} else if (memberRepository.get((int)loginMemberId).getGrade() == Grade.STANDARD) {
			//회원화면
			
			while (!sel.equals("q")) {
				loginMainScreen();
			}
			
			
		} else if (memberRepository.get((int)loginMemberId).getGrade() == Grade.ADMIN) {
			//관리자화면
			
			while (!sel.equals("q")) {
				adminMainScreen();
			}
		}
			
		quit();
	}
	
	
	
	//프로그램 실행
	
	private static void loadItem() throws Exception {
	
		HashMap<String, String[]> itemMaterials = loadItemMaterial(Path.ITEM_MATERIAL.getPath());

		BufferedReader reader = new BufferedReader(new FileReader(Path.ITEM.getPath()));
		String line = "";
		while((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			Item item = new Item();
			item.setCode(temp[0]);
			item.setBrand(temp[1]);
			item.setName(temp[2]);
			item.setWeight(Integer.parseInt(temp[3]));
			item.setSize(temp[4]);
			item.setShape(Shape.valueOf(temp[5]));
			
			ArrayList<Material> materials = new ArrayList<Material>();
			
			String[] materialArray = itemMaterials.get(item.getCode());
			
			for (int i = 1; i < materialArray.length; ++i) {
				materials.add(Material.valueOf(materialArray[i]));
			}

			item.setMaterial(materials);
			
			itemRepository.add(item);
			brandList.add(item.getBrand());
		}
		reader.close();
	}

	
	private static HashMap<String, String[]> loadItemMaterial (String path) throws Exception {
		
		HashMap<String, String[]> itemMaterials = new HashMap<String, String[]>();

		BufferedReader reader = new BufferedReader(new FileReader(Path.ITEM_MATERIAL.getPath()));
		String line = null;
		while((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			itemMaterials.put(temp[0], temp);
		}
		reader.close();

		return itemMaterials;
	}
	
	
	private static void loadMember() throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(Path.MEMBER.getPath()));
		String line = null;
		while((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			Member member = new Member();
			member.setCode(Long.parseLong(temp[0]));
			member.setId(temp[1]);
			member.setPassword(temp[2]);
			member.setName(temp[3]);
			member.setTel(temp[4]);
			member.setBirth(temp[5]);
			member.setAddress(temp[6]);
			member.setGrade(Grade.valueOf(temp[7].toUpperCase()));
			
			memberRepository.add(member);
		}
		reader.close();
	}
	
	
	private static void loadOrder() throws Exception {

		ArrayList<OrderItemTag> orderItemTags = loadOrderItemTag(Path.ORDER_ITEM_TAG.getPath());
		HashMap<Long, PayInfo> payInfos = new HashMap<Long, PayInfo>();
		payInfos.putAll(loadCardPayInfo(Path.CARD_PAY_INFO.getPath()));
		payInfos.putAll(loadBankbookPayInfo(Path.BANKBOOK_PAY_INFO.getPath()));
		HashMap<Long, OrderTimeStamp> orderTimeStamps = loadOrderTimeStamp(Path.ORDER_TIME_STAMP.getPath());
		HashMap<Long, PersonCard> orderMans = loadPersonCard(Path.ORDER_MAN.getPath());
		HashMap<Long, PersonCard> receivers = loadPersonCard(Path.RECEIVER.getPath());

		BufferedReader reader = new BufferedReader(new FileReader(Path.ORDER.getPath()));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			Order order = new Order();
			order.setCode(Long.parseLong(temp[0]));
			order.setMemberCode(Long.parseLong(temp[1]));
			order.setAddress(temp[2]);
			order.setStatus(OrderStatus.valueOf(temp[3].toUpperCase()));
			
			ArrayList<OrderItemTag> itemInfo = new ArrayList<OrderItemTag>();
			
			for (OrderItemTag orderItemTag : orderItemTags) {
				if (order.getCode() == orderItemTag.getOrderCode()) {
					itemInfo.add(orderItemTag);
				}
			}
			
			order.setItemInfo(itemInfo);
			order.setPayInfo(payInfos.get(order.getCode()));
			order.setTimeStamp(orderTimeStamps.get(order.getCode()));
			order.setOrderMan(orderMans.get(order.getCode()));
			order.setReceiver(receivers.get(order.getCode()));
			
			orderRepository.add(order);
		}
		reader.close();
	}
	

	private static ArrayList<OrderItemTag> loadOrderItemTag (String path) throws Exception {

		ArrayList<OrderItemTag> orderItemTags = new ArrayList<OrderItemTag>();

		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			OrderItemTag orderItemTag = new OrderItemTag();
			orderItemTag.setItemCode(temp[0]);
			orderItemTag.setCount(Integer.parseInt(temp[1]));
			orderItemTag.setPrice(Integer.parseInt(temp[2]));
			orderItemTag.setOrderCode(Long.parseLong(temp[3]));
			
			orderItemTags.add(orderItemTag);
		}
		reader.close();

		return orderItemTags;
	}
	
	
	private static HashMap<Long, OrderTimeStamp> loadOrderTimeStamp (String path) throws Exception {
		
		HashMap<Long, OrderTimeStamp> orderTimeStamps = new HashMap<Long, OrderTimeStamp>();

		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			ArrayList<Calendar> timeStamps = new ArrayList<Calendar>();
			
			String[] temp = line.split(",");
			
			for (int i = 1; i < temp.length; ++i) {
				
				if (!temp[i].equals("null")) {
					
					String[] dateNums = temp[i].split("-");
					Calendar calendar = Calendar.getInstance();
					calendar.set(Integer.parseInt(dateNums[0])
								, Integer.parseInt(dateNums[1])
								, Integer.parseInt(dateNums[2])
								, Integer.parseInt(dateNums[3])
								, Integer.parseInt(dateNums[4]));
					
					timeStamps.add(calendar);
				} else {
					timeStamps.add(null);
				}
			}
			
			OrderTimeStamp orderTimeStamp = new OrderTimeStamp();
			orderTimeStamp.setOrderTime(timeStamps.get(0));
			orderTimeStamp.setDeliveryTime(timeStamps.get(1));
			orderTimeStamp.setPurchaseConfirmTime(timeStamps.get(2));
			orderTimeStamp.setExtraRequestTime(timeStamps.get(3));
			orderTimeStamp.setExtraDoneTime(timeStamps.get(4));
			
			orderTimeStamps.put(Long.parseLong(temp[0]), orderTimeStamp);
		}
		reader.close();

		return orderTimeStamps;
	}
	
	
	private static HashMap<Long, PersonCard> loadPersonCard (String path) throws Exception {
		
		HashMap<Long, PersonCard> personCards = new HashMap<Long,PersonCard>();
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			PersonCard personCard = new PersonCard();
			personCard.setName(temp[1]);
			personCard.setTel(temp[2]);
			
			personCards.put(Long.parseLong(temp[0]), personCard);
		}
		reader.close();

		return personCards;
	}
	
	
	private static HashMap<Long, PayInfo> loadCardPayInfo(String path) throws Exception {
		
		HashMap<Long, PayInfo> payInfos = new HashMap<Long, PayInfo>();
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			CardPayInfo cardPayInfo = new CardPayInfo();
			cardPayInfo.setPayOption(PayOption.valueOf(temp[1]));
			cardPayInfo.setPrice(Integer.parseInt(temp[2]));
			cardPayInfo.setStatus(Boolean.parseBoolean(temp[3]));
			cardPayInfo.setCardNum(temp[4]);
			
			payInfos.put(Long.parseLong(temp[0]), cardPayInfo);
		}
		reader.close();

		return payInfos;
	}

	
	private static HashMap<Long, PayInfo> loadBankbookPayInfo(String path) throws Exception {
		
		HashMap<Long, PayInfo> payInfos = new HashMap<Long, PayInfo>();
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = null;
		while ((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			BankbookPayInfo bankbookPayInfo = new BankbookPayInfo();
			bankbookPayInfo.setPayOption(PayOption.valueOf(temp[1]));
			bankbookPayInfo.setPrice(Integer.parseInt(temp[2]));
			bankbookPayInfo.setStatus(Boolean.parseBoolean(temp[3]));
			bankbookPayInfo.setBankbookNum(temp[4]);
			
			payInfos.put(Long.parseLong(temp[0]), bankbookPayInfo);
		}
		reader.close();

		return payInfos;
	}

	
	private static void loadItemTag() throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(Path.ITEM_TAG.getPath()));
		String line = null;
		while((line = reader.readLine()) != null) {
			
			String[] temp = line.split(",");
			
			ItemTag itemTag = new ItemTag();
			itemTag.setItemCode(temp[0]);
			itemTag.setCount(Integer.parseInt(temp[1]));
			itemTag.setPrice(Integer.parseInt(temp[2]));
			
			itemTagRepository.put(itemTag.getItemCode(), itemTag);
		}
		reader.close();
	}
	

	
	//공통사용 메서드
	
	private static void pause() {
		pause("");
	}
	
	
	private static void pause(String message) {
		
		if (!message.equals("")) {
			System.out.println(message);
		}
		System.out.println("계속하시려면 엔터를 누르세요.");
		scan.nextLine();
	}

	
	
	//메인화면
	
	private static void mainScreen() {
		
		/*
		요구사항 RQ-01-00-00 ~
		 */
		
		while (!sel.equals("q")) {
			
			itemListScreen();
				totalListScreen();
					totalSerchScreen();
				categoryListScreen();
					shapeCategoryScreen();
					materialCategoryScreen();
					brandCategoryScreen();
				totalSerchScreen();
					searchResultScreen();
			basketScreen();
				itemCountChange();
				itemRemove();
				itemOrderScreen(); //비회원 주문
			loginScreen();
				joinScreen();
				idSearch();
				passwordSearch();
			joinScreen();
			orderLookupScreen();
				orderDetailScreen();
		}
		
	}

	
	private static void loginMainScreen() {

		/*
		요구사항 RQ-01-10-00 ~
		 */
		
		while (!sel.equals("q")) {

			itemListScreen();
				totalListScreen();
					totalSerchScreen();
				categoryListScreen();
					shapeCategoryScreen();
					materialCategoryScreen();
					brandCategoryScreen();
				totalSerchScreen();
					searchResultScreen();
			basketScreen();
				itemCountChange();
				itemRemove();
				itemOrderScreen();
			myInfoScreen();
				orderHistoryScreen();
					orderDetailScreen();
				myInfoChangeScreen();
		}
	}

	
	private static void adminMainScreen() {

		/*
		요구사항 RQ-01-20-00 ~
		 */
		
		while (!sel.equals("q")) {
			
			salesManagementScreen();
			orderManagementScreen();
			 	orderSearch();
				orderDetailScreen();
			invenManagementScreen();
				newItemScreen();
				itemChangeScreen();
				adminOrder();
			memberManagementScreen();
				memberDetailScreen();
			
		}

	}

	
	
	//유저 화면
	
	private static void itemListScreen() {

		/*
		요구사항 RQ-02-00-00 ~
		 */
		
		while(!(sel.equals("q") || sel.equals("m"))) {
			System.out.println("\nsel = " + sel);
			
			System.out.println("[전체상품목록]");
			System.out.println("1.상품검색 u.이전단계 m.메인 q.종료");
			System.out.print("입력 : ");
			sel =scan.nextLine();

			if (sel.equals("1")) {
				
				totalListScreen();
				
			} else if (sel.equals("u")) {
				return;
			}
		}

		
	}

	
	private static void totalListScreen() {

		/*
		요구사항 RQ-02-10-00 ~
		 */

	}

	
	private static void categoryListScreen() {

		/*
		요구사항 RQ-02-20-00 ~
		 */

	}

	
	private static void shapeCategoryScreen() {
		// TODO Auto-generated method stub
		
	}

	
	private static void materialCategoryScreen() {
		// TODO Auto-generated method stub
		
	}

	
	private static void brandCategoryScreen() {
		// TODO Auto-generated method stub
		
	}


	private static void totalSerchScreen() {

		/*
		요구사항 RQ-02-30-00 ~
		 */

	}

	
	private static void searchResultScreen() {
		// TODO Auto-generated method stub
		
	}

	
	private static void basketScreen() {

		/*
		요구사항 RQ-03-00-00 ~
		 */

	}

	
	private static void itemCountChange() {
		
	}

	
	private static void itemRemove() {
		// TODO Auto-generated method stub
		
	}

	
	private static void itemOrderScreen() {
		
		/*
		요구사항 RQ-03-40-00 ~
		*/
	}
	

	private static void loginScreen() {

		/*
		요구사항 RQ-04-00-00 ~
		 */
		
	}

	
	private static void joinScreen() {

		/*
		요구사항 RQ-04-20-00 ~
		 */

	}

	
	private static void idSearch() {
		
		/*
		요구사항 RQ-04-30-00 ~
		*/
		
	}

	
	private static void passwordSearch() {
		
		/*
		요구사항 RQ-04-40-00 ~
		*/
		
	}

	
	private static void orderLookupScreen() {

		/*
		요구사항 RQ-05-00-00 ~
		 */

	}

	
	private static void orderDetailScreen() {
		
		/*
		요구사항 RQ-06-10-00 ~
		*/
		
	}

	
	
	private static void myInfoScreen() {
		
		/*
		요구사항 RQ-06-00-00 ~
		*/
		
	}


	
	private static void orderHistoryScreen() {
		
		/*
		요구사항 RQ-06-10-00 ~
		*/
		
	}
	
	
	
	private static void myInfoChangeScreen() {
		
		/*
		요구사항 RQ-06-20-00 ~
		*/
		
	}

	
	//관리자 화면
	
	
	
	
	//관리자 화면
	
	private static void salesManagementScreen() {
		
		/*
		요구사항 RQ-07-00-00 ~
		*/
		
	}
	
	

	private static void orderManagementScreen() {
		
		/*
		요구사항 RQ-08-00-00 ~
		*/
		
	}
	
	

	private static void orderSearch() {
		
		/*
		요구사항 RQ-08-20-00 ~
		요구사항도 미완성이니 알아서 잘 구현하십시오. 화이팅 ㅎㅎ;;
		*/
		
	}

	
	
	private static void invenManagementScreen() {
		
		/*
		요구사항 RQ-09-00-00 ~
		*/
		
	}
	
	
	
	private static void newItemScreen() {
		
		/*
		요구사항 RQ-09-10-00 ~
		*/
		
	}

	
	
	private static void itemChangeScreen() {
		
		/*
		요구사항 RQ-09-20-00 ~
		*/
		
	}

	
	
	private static void adminOrder() {
		
		/*
		요구사항 RQ-09-30-00 ~
		*/
		
	}


	
	private static void memberManagementScreen() {
		
		/*
		요구사항 RQ-10-00-00 ~
		*/
		
	}

	
	
	private static void memberDetailScreen() {
		
		/*
		요구사항 RQ-10-10-00 ~
		*/
		
	}

	

	//프로그램 종료
	
	private static void quit() {
		
		System.out.println("저장중입니다.");
		
		try {

			saveItem();
			saveMember();
			saveOrder();
			saveItemTag();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("[ERROR] 에러 꼭 찾고 넘어가세요.");
		}
		
		System.out.println("종료되었습니다.");
	}



	private static void saveItem() throws Exception {
		
		BufferedWriter itemWriter = new BufferedWriter(new FileWriter(Path.ITEM.getPath()));
		BufferedWriter itemMaterialWriter = new BufferedWriter(new FileWriter(Path.ITEM_MATERIAL.getPath()));
		
		for (Item item : itemRepository) {
			String tempItem = String.format("%s,%s,%s,%s,%s,%s", item.getCode()
															,item.getBrand()
															,item.getName()
															,item.getWeight()
															,item.getSize()
															,item.getShape());
			
			itemWriter.append(tempItem);
			itemWriter.newLine();
			
			String tempItemMaterial = item.getCode();
			for (Material material : item.getMaterial()) {
				
				tempItemMaterial += String.format("," + material.toString());
			}
			
			itemMaterialWriter.append(tempItemMaterial);
			itemMaterialWriter.newLine();
		}
		itemWriter.close();
		itemMaterialWriter.close();
	}

	
	private static void saveMember() throws Exception {
	
		BufferedWriter writer = new BufferedWriter(new FileWriter(Path.MEMBER.getPath()));
		
		for (Member member : memberRepository) {
			
			String tempMember = String.format("%s,%s,%s,%s,%s,%s,%s,%s", member.getCode()
																	, member.getId()
																	, member.getPassword()
																	, member.getName()
																	, member.getTel()
																	, member.getBirth()
																	, member.getAddress()
																	, member.getGrade().toString());
			
			writer.append(tempMember);
			writer.newLine();
		}
		writer.close();
	}



	private static void saveOrder() throws Exception {
		
		BufferedWriter orderWriter = new BufferedWriter(new FileWriter(Path.ORDER.getPath()));
		BufferedWriter orderItemTagWriter = new BufferedWriter(new FileWriter(Path.ORDER_ITEM_TAG.getPath()));
		BufferedWriter orderTimeStampWriter = new BufferedWriter(new FileWriter(Path.ORDER_TIME_STAMP.getPath()));
		BufferedWriter orderManWriter = new BufferedWriter(new FileWriter(Path.ORDER_MAN.getPath()));
		BufferedWriter receiverWriter = new BufferedWriter(new FileWriter(Path.RECEIVER.getPath()));
		BufferedWriter cardPayInfoWriter = new BufferedWriter(new FileWriter(Path.CARD_PAY_INFO.getPath()));
		BufferedWriter bankbookPayInfoWriter = new BufferedWriter(new FileWriter(Path.BANKBOOK_PAY_INFO.getPath()));
		
		for (Order order : orderRepository) {
			
			String tempOrder = String.format("%s,%s,%s,%s", order.getCode()
															, order.getMemberCode()
															, order.getAddress()
															, order.getStatus().toString());
			
			orderWriter.append(tempOrder);
			orderWriter.newLine();
			
			ArrayList<OrderItemTag> orderItemTags = order.getItemInfo();
			
			for (OrderItemTag orderItemTag : orderItemTags) {
				
				String tempOderItemTag = String.format("%s,%s,%s,%s", orderItemTag.getItemCode()
																	, orderItemTag.getCount()
																	, orderItemTag.getPrice()
																	, orderItemTag.getOrderCode());
				
				orderItemTagWriter.append(tempOderItemTag);
				orderItemTagWriter.newLine();
			}
			
			OrderTimeStamp orderTimeStamp = order.getTimeStamp();
			
			Calendar orderTime = orderTimeStamp.getOrderTime();
			Calendar deliveryTime = orderTimeStamp.getDeliveryTime();
			Calendar purchaseConfirmTime = orderTimeStamp.getPurchaseConfirmTime();
			Calendar extraRequestTime = orderTimeStamp.getExtraRequestTime();
			Calendar extraDoneTime = orderTimeStamp.getExtraDoneTime();
			
			
			String tempOrderTime = orderTime == null ? null : String.format("%tF-%tH-%tM", orderTime, orderTime, orderTime);
			String tempDeliveryTime = deliveryTime == null ? null : String.format("%tF-%tH-%tM", deliveryTime, deliveryTime, deliveryTime);
			String tempPurchaseConfirmTime = purchaseConfirmTime == null ? null : String.format("%tF-%tH-%tM", purchaseConfirmTime, purchaseConfirmTime, purchaseConfirmTime);
			String tempExtraRequestTime = extraRequestTime == null ? null : String.format("%tF-%tH-%tM", extraRequestTime, extraRequestTime, extraRequestTime);
			String tempExtraDoneTime = extraDoneTime == null ? null : String.format("%tF-%tH-%tM", extraDoneTime, extraDoneTime, extraDoneTime);
			
			String tempOrderTimeStamp = String.format("%s,%s,%s,%s,%s,%s", order.getCode()
																			, tempOrderTime
																			, tempDeliveryTime
																			, tempPurchaseConfirmTime
																			, tempExtraRequestTime
																			, tempExtraDoneTime);
			
			orderTimeStampWriter.append(tempOrderTimeStamp);
			orderTimeStampWriter.newLine();
			
			PersonCard orderMan = order.getOrderMan();
			String tempOrderMan = String.format("%s,%s,%s", order.getCode()
															, orderMan.getName()
															, orderMan.getTel());
			
			orderManWriter.append(tempOrderMan);
			orderManWriter.newLine();
			
			PersonCard receiver = order.getReceiver();
			String tempreceiver = String.format("%s,%s,%s", order.getCode()
															, receiver.getName()
															, receiver.getTel());
			
			receiverWriter.append(tempreceiver);
			receiverWriter.newLine();
			
			
			if (order.getPayInfo().getClass().equals(CardPayInfo.class)) {
				
				CardPayInfo cardPayInfo = (CardPayInfo)order.getPayInfo();
				
				String tempCardPayInfo = String.format("%s,%s,%s,%s,%s", order.getCode() 
																		, cardPayInfo.getPayOption().toString()
																		, cardPayInfo.getPrice()
																		, cardPayInfo.isStatus()
																		, cardPayInfo.getCardNum());
				
				cardPayInfoWriter.append(tempCardPayInfo);
				cardPayInfoWriter.newLine();
						
			} else if (order.getPayInfo().getClass().equals(BankbookPayInfo.class)) {
			
				
				
				BankbookPayInfo bankbookPayInfo = (BankbookPayInfo)order.getPayInfo();
				
				String tempBankbookPayInfo = String.format("%s,%s,%s,%s,%s", order.getCode() 
																			, bankbookPayInfo.getPayOption().toString()
																			, bankbookPayInfo.getPrice()
																			, bankbookPayInfo.isStatus()
																			, bankbookPayInfo.getBankbookNum());
				
				bankbookPayInfoWriter.append(tempBankbookPayInfo);
				bankbookPayInfoWriter.newLine();
			}
		}
		orderWriter.close();
		orderItemTagWriter.close();
		orderTimeStampWriter.close();
		orderManWriter.close();
		receiverWriter.close();
		cardPayInfoWriter.close();
		bankbookPayInfoWriter.close();
	}


	private static void saveItemTag() throws Exception {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(Path.ITEM_TAG.getPath()));
		
		Set<String> keys = itemTagRepository.keySet();
		for (String key : keys) {
			
			ItemTag itemTag = itemTagRepository.get(key);
			
			String tempItemTag = String.format("%s,%s,%s", itemTag.getItemCode()
														, itemTag.getCount()
														, itemTag.getPrice());
			
			writer.append(tempItemTag);
			writer.newLine();
		}
		writer.close();
	}


}
