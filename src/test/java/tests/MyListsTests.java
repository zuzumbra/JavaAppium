package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.*;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListsTests extends CoreTestCase {
    private static final String name_of_folder = "Learning programming";
    private static final String login = "Zuzumbra";
    private static final String password = "30211yrb";

    @Test
    public void testSaveFirstArticleToMyList(){

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        articlePageObject.waitForTitleElement();
        String article_title = articlePageObject.getArticleTitle();

/*
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

        if(Platform.getInstance().isAndroid()){
            articlePageObject.addArticleToMyListToNewFolder(name_of_folder);
        } else {
            articlePageObject.addArticlesToMySaved();
        }
        if(Platform.getInstance().isIOS()){
            articlePageObject.closePopUp();
        }

        if  (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();

            articlePageObject.waitForTitleElement();

            assertEquals("We are not on the same page after login", article_title, articlePageObject.getArticleTitle());

            articlePageObject.addArticlesToMySaved();
        }

        articlePageObject.closeArticle();

        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        navigationUI.openNavigation();
        navigationUI.clickMyList();

        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);

        if(Platform.getInstance().isAndroid()){
            myListsPageObject.openFolderByName(name_of_folder);
        }

        myListsPageObject.swipeByArticleToDelete(article_title);
    }


    @Test
    public void testSaveTwoArticlesToMyListAndDeleteOneOfThem() {

        String search_line = "Quantum field theory";
        String article_subtitle_1 = "Physical theories";
        String article_subtitle_2 = "theoretical framework";
        String name_of_folder = "Physics";

        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

        searchPageObject.initSearchInput();
        searchPageObject.clearSearchInput();
        searchPageObject.typeSearchLine(search_line);
        searchPageObject.clickByArticleWithSubstring(article_subtitle_1);

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        String article_title_1 = articlePageObject.getArticleTitle();
        String expected_string = articlePageObject.getArticleSubtitle();

        if(Platform.getInstance().isAndroid()){
            articlePageObject.addArticleToMyListToNewFolder(name_of_folder);
        } else {
            articlePageObject.addArticlesToMySaved();
        }
        if(Platform.getInstance().isIOS()){
            articlePageObject.closePopUp();
        }

        if  (Platform.getInstance().isMW()) {
            AuthorizationPageObject Auth = new AuthorizationPageObject(driver);
            Auth.clickAuthButton();
            Auth.enterLoginData(login, password);
            Auth.submitForm();

            articlePageObject.waitForTitleElement();

            assertEquals("We are not on the same page after login", expected_string, articlePageObject.getArticleSubtitle());

            articlePageObject.addArticlesToMySaved();
        }

        articlePageObject.closeArticle();

        searchPageObject.initSearchInput();
        searchPageObject.clearSearchInput();
        searchPageObject.typeSearchLine(search_line);
        searchPageObject.clickByArticleWithSubstring(article_subtitle_2);
        String article_title_2 = articlePageObject.getArticleTitle();

        if(Platform.getInstance().isAndroid()){
            articlePageObject.addArticleToMyListToSavedFolder(name_of_folder);
        } else {
            articlePageObject.addArticlesToMySaved();
        }

        articlePageObject.closeArticle();

        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        navigationUI.openNavigation();
        navigationUI.clickMyList();

        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);

        if (Platform.getInstance().isAndroid()) {
            myListsPageObject.openFolderByName(name_of_folder);
        }
        myListsPageObject.swipeByArticleToDelete(article_title_2);
        myListsPageObject.waitForArticleAndClick(article_title_1);

        assertEquals(
                "We see unexpected article",
                expected_string,
                articlePageObject.getArticleSubtitle()
        );
    }
}
