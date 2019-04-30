package a.test;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
public class HtmlUtilTest {
	 	@Test
	    public void test() {
	        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

	        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
	        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
	        webClient.getOptions().setActiveXNative(false);
	        webClient.getOptions().setCssEnabled(true);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
	        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
	        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
	        webClient.getCookieManager().setCookiesEnabled(true);
	        webClient.getOptions().setUseInsecureSSL(true);

	        HtmlPage page = null;
	        try {
	        	//page = webClient.getPage("https://www.zhihu.com/topics");//尝试加载上面图片例子给出的网页
	             page = webClient.getPage("https://www.zhihu.com/signin?next=%2Ftopics");
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally {
	            webClient.close();
	        }

	        webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束

	        //cookies 获取
	        CookieManager CM = webClient.getCookieManager(); //WC = Your WebClient's name  
            Set<Cookie> cookiesSet = CM.getCookies();
            for(Cookie cookie:cookiesSet) {
            	System.out.println(cookie.getName()+"="+cookie.getValue());
            }
            
            
           
	        
	        HtmlPage  ele = page.getPage();
	       // String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
	        
	        login(ele,CM);
	      
	        
	        
	        
	        

	        //TODO 下面的代码就是对字符串的操作了,常规的爬虫操作,用到了比较好用的Jsoup库

	       /* Document document = Jsoup.parse(pageXml);//获取html文档
	        List<Element> infoListEle = document.getElementById("feedCardContent").getElementsByAttributeValue("class", "feed-card-item");//获取元素节点等
	        infoListEle.forEach(element -> {
	            System.out.println(element.getElementsByTag("h2").first().getElementsByTag("a").text());
	            System.out.println(element.getElementsByTag("h2").first().getElementsByTag("a").attr("href"));
	        });*/
	    }
	 	
	 	private void login(HtmlPage page,CookieManager CM) {
	 		  //登录验证
	 		
          
           
	        List<DomElement> usernameInput = page.getElementsByName("username");
	        List<DomElement> passwordInput = page.getElementsByName("password");
	        HtmlButton  submit =page.getFirstByXPath("//button[@type=\"submit\"]");
	        
	        HtmlPage loginResultpage = null;
	        
	        HtmlInput unInput=(HtmlInput) usernameInput.get(0);
	        HtmlInput pwInput=(HtmlInput) passwordInput.get(0);
	        
	        unInput.setAttribute("value","18521512271");
	        pwInput.setAttribute("value","meng1234567");
	        try {
				loginResultpage =submit.click();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
	        Set<Cookie> cookiesSet = CM.getCookies();
	           for(Cookie cookie:cookiesSet) {
	           	System.out.println(cookie.getName()+"="+cookie.getValue());
	           }
	        System.out.println(loginResultpage.asXml());
	 	}
	 	
	 
}
