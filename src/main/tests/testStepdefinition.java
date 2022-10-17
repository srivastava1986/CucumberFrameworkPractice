import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.util.*;

public class testStepdefinition {

    WebDriver driver;

    @Given("I launch Chrome Browser")
    public void i_launch_chrome_browser() {


        System.setProperty("webdriver.chrome.driver", "C://WebAutomationUsingCucumber//Drivers//Chrome//chromedriver.exe");
        //opening in incongnito mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        //maximize the window
        driver.manage().window().maximize();

    }

    @When("I open Amazon homepage")
    public void i_open_amazon_homepage() {
        driver.get("https://www.amazon.in/");
    }

    @Then("I add product into the cart")
    public void i_add_product_into_the_cart() throws IOException {
        //Go to Electronics
        driver.findElement(By.xpath("//*[@data-csa-c-slot-id='nav_cs_5']")).click();
        //Search for OnePlus
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("OnePlus");
        //Click on search icon
        driver.findElement(By.id("nav-search-submit-button")).click();
        //Click on first item
        driver.findElement(By.xpath("//*[@data-image-index='1']")).click();

        //navigate to another window
        //driver.switchTo().window("https://www.amazon.in/OnePlus-Jade-Green-256GB-Storage/dp/B0B5V4WWRP/ref=sr_1_2_sspa?crid=3MW7CLWHDP5YC&keywords=OnePlus&qid=1665968755&qu=eyJxc2MiOiI1LjQ1IiwicXNhIjoiNS4wMyIsInFzcCI6IjQuNDQifQ%3D%3D&s=electronics&sprefix=oneplus%2Celectronics%2C128&sr=1-2-spons&psc=1");
        String parent=driver.getWindowHandle();

        ArrayList<String> windows=new ArrayList<String>(driver.getWindowHandles());

        driver.switchTo().window(windows.get(1));

        JavascriptExecutor js= (JavascriptExecutor) driver;

        js.executeScript("window.scrollBy(0,1000)");

        List<WebElement> getallLi=driver.findElements(By.xpath("//*[@id=\"feature-bullets\"]/ul/li"));

        String description;
        String s1="";

        for (int i=1;i<=getallLi.size();i++){
            description=driver.findElement(By.xpath("//*[@id=\"feature-bullets\"]/ul/li"+"["+i+"]")).getText();
            s1=s1+description;
        }

        System.out.println(s1);
        writeinPropertiesFile(s1);

        //switch back to the parent window

        driver.switchTo().window(windows.get(0));

        List<WebElement> productpagelisting=driver.findElements(By.xpath("//*[@class=\"a-size-mini a-spacing-none a-color-base s-line-clamp-2\"]"));

        String productname;
        String price;


        // This data needs to be written (Object[])

        XSSFWorkbook workbook = new XSSFWorkbook();

        for (int j=1;j<=productpagelisting.size();j++){
            productname=driver.findElement(By.xpath("//*[@class=\"a-size-mini a-spacing-none a-color-base s-line-clamp-2\"]/a/span")).getText();
            price=driver.findElement(By.xpath("//*[@class=\"a-section a-spacing-none a-spacing-top-small s-price-instructions-style\"]/div[2]/a/span/span[1]")).getText();
            System.out.println(price);


            XSSFSheet spreadsheet = workbook.createSheet(" Product_Data ");
            XSSFRow row;

            // creating a row object
            Map<String, Object[]> productdata = new TreeMap<String, Object[]>();

            productdata.put("1",new Object[]{"ProductName","ProductPrice"});
            productdata.put(String.valueOf(j+1),new Object[]{productname,price});


            Set<String> keyid = productdata.keySet();

            int rowid = 0;

            for (String key : keyid){
                row=spreadsheet.createRow(rowid++);

                Object[] objectArr = productdata.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String)obj);
                }
            }

            // .xlsx is the format for Excel Sheets...
            // writing the workbook into the file...







        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(
                    new File("C:/CucumberFrameworkPractice/productdata.xlsx"));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }




        driver.switchTo().window(windows.get(1));
        js.executeScript("window.scrollBy(0,500)");
        driver.findElement(By.id("add-to-cart-button")).click();
        driver.findElement(By.xpath("")).click();


    }
    @And("close Browser")
    public void close_browser() {
    //driver.quit();

            }


    public void writeinPropertiesFile(String description){
        String propertiesfilepath="C://CucumberFrameworkPractice//Description.properties";

        try {
            FileOutputStream file=new FileOutputStream(propertiesfilepath);
            Properties prop=new Properties();
            prop.clear();
            prop.setProperty("Description", description);
            prop.store(file,null);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void writeinExcelFile(String productdescription, String price, int i) throws IOException {


        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(" Product_Data ");
        // creating a row object
        XSSFRow row;

        // This data needs to be written (Object[])
        Map<String, Object[]> productdata = new TreeMap<String, Object[]>();

        productdata.put("1",new Object[]{"ProductName","ProductPrice"});
        productdata.put(String.valueOf(i+1),new Object[]{productdescription,price});

        Set<String> keyid = productdata.keySet();

        int rowid = 0;

        for (String key : keyid){
            row=spreadsheet.createRow(rowid++);

            Object[] objectArr = productdata.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(
                    new File("C:/CucumberFrameworkPractice/productdata.xlsx"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        workbook.write(out);
        out.close();






    }



}


