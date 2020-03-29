package com.web.service;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class TestData {
	public static Service montrealService;
	public static Service sherbrookService;
	public static Service quebecService;

	public static void main(String[] args) throws Exception {
//
		URL montrealURL = new URL("http://localhost:8080/montreal?wsdl");
		QName montrealQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		montrealService = Service.create(montrealURL, montrealQName);

		URL quebecURL = new URL("http://localhost:8080/quebec?wsdl");
		QName quebecQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		quebecService = Service.create(quebecURL, quebecQName);

		URL sherbrookURL = new URL("http://localhost:8080/sherbrook?wsdl");
		QName sherbrookQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		sherbrookService = Service.create(sherbrookURL, sherbrookQName);

		addTestData();
	}

	private static void addTestData() {
		WebInterface MTLobj = montrealService.getPort(WebInterface.class);
		WebInterface QUEobj = quebecService.getPort(WebInterface.class);
		WebInterface SHEobj = sherbrookService.getPort(WebInterface.class);

		//MTL
//		System.out.println(MTLobj.addEvent("Conferences", "MTLM230320", 1));
//		System.out.println(MTLobj.addEvent( "Seminars", "MTLM250320", 2));
//		MTLobj.printDatabase();

		System.out.println(MTLobj.addEvent("Trade_Shows", "MTLM230320", 3));
		System.out.println(MTLobj.addEvent("Trade_Shows", "MTLE230320", 3));

		//SHE
//		System.out.println(SHEobj.addEvent("Conferences", "SHEM110320", 1));
//		System.out.println(SHEobj.addEvent("Seminars", "SHEM120320", 2));
//		System.out.println(SHEobj.addEvent("Seminars", "SHEM130320", 3));
//		System.out.println(SHEobj.addEvent("Seminars", "SHEM100320", 4));
//		SHEobj.addEvent( "Seminars", "MTLM250320", 2);
//
//
//		System.out.println(MTLobj.toString());
//		System.out.println(MTLobj.bookEvent("MTLC1234", "SHEM110320", "Conferences"));
//		System.out.println(MTLobj.bookEvent("MTLC1234", "SHEM120320", "Seminars"));
//		System.out.println(MTLobj.bookEvent("MTLC1234", "SHEM130320", "Seminars"));
//		System.out.println(MTLobj.bookEvent("MTLC1234", "SHEM100320", "Seminars"));
//		//System.out.println(MTLobj.bookEvent("MTLC1234", "SHEM270320", "Seminars"));

		System.out.println(MTLobj.toString());
		System.out.println(MTLobj.bookEvent("MTLC1234", "MTLM230320", "Trade_Shows"));
		System.out.println(MTLobj.bookEvent("MTLC1235", "MTLM230320", "Trade_Shows"));
		System.out.println(MTLobj.bookEvent("SHEC1236", "MTLM230320", "Trade_Shows"));


		//System.out.println(MTLobj.getBookingSchedul("MTLC1234"));
	}
}
