package com.android.msahakyan.marsrover;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.android.msahakyan.marsrover.activity.MainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock(name = "mainActivity")
    private MainActivity mainActivity;

    @Before
    public void setUp() {
        when(mainActivity.deleteFile("")).thenReturn(true);
        when(mainActivity.checkCallingOrSelfPermission("Permission")).thenReturn(56);
    }

    @Test
    public void testFileDelete() {
        Assert.assertEquals(mainActivity.deleteFile(""), true);
    }

    @Test
    public void testPermission() {
        Assert.assertNotNull(mainActivity);
        Assert.assertEquals(mainActivity.checkCallingOrSelfPermission("Permission"), 56);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


//    @Test
//    public void onClick_WithRealIntent() throws Exception {
//        Activity mockActivity = Mockito.mock(Activity.class);
//        View mockView = Mockito.mock(View.class);
//
//        MyOnclickListener testObject = new MyOnclickListener(mockActivity);
//        testObject.onClick(mockView);
//
//        Intent expectedIntent = new Intent("com.google.zxing.client.android.SCAN");
//        Mockito.verify(mockActivity).startActivityForResult(expectedIntent, 1);
//    }

    @Test
    public void onClick_startsActivity_WithTheRightIntent() throws Exception {
//        Activity mockActivity = Mockito.mock(Activity.class);
//        Intent mockIntent = Mockito.mock(Intent.class);
//
//        Mockito.when(mockIntentProvider.provideIntent()).thenReturn(mockIntent);
//
//        MyOnclickListener testObject = new MyOnclickListener(mockActivity, mockIntentProvider);
//        View mockView = Mockito.mock(View.class);
//        testObject.onClick(mockView);
//
//        Mockito.verify(mockActivity).startActivityForResult(mockIntent, 1);
    }

}