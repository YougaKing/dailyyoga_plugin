/*
 * Created by wangzhuozhou on 2015/08/12.
 * Copyright 2015－2020 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dailyyoga.plugin.miit

import org.objectweb.asm.Opcodes

class DailyyogaMIITHookConfig {
    public static final String SENSORS_ANALYTICS_API = "com/sensorsdata/analytics/android/sdk/SensorsDataAutoTrackHelper"
    public final static HashMap<String, DailyyogaMIITMethodCell> INTERFACE_METHODS = new HashMap<>()
    public final static HashMap<String, DailyyogaMIITMethodCell> CLASS_METHODS = new HashMap<>()

    static {
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'android/widget/CompoundButton$OnCheckedChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'android/widget/RatingBar$OnRatingBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onStopTrackingTouch',
                '(Landroid/widget/SeekBar;)V',
                'android/widget/SeekBar$OnSeekBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'android/widget/RadioGroup$OnCheckedChangeListener',
                'trackRadioGroup',
                '(Landroid/widget/RadioGroup;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'android/content/DialogInterface$OnClickListener',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onItemSelected',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemSelectedListener',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'android/widget/ExpandableListView$OnGroupClickListener',
                'trackExpandableListViewOnGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'android/widget/ExpandableListView$OnChildClickListener',
                'trackExpandableListViewOnChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
                1, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onTabChanged',
                '(Ljava/lang/String;)V',
                'android/widget/TabHost$OnTabChangeListener',
                'trackTabHost',
                '(Ljava/lang/String;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onTabSelected',
                '(Landroid/support/design/widget/TabLayout$Tab;)V',
                'android/support/design/widget/TabLayout$OnTabSelectedListener',
                'trackTabLayoutSelected',
                '(Ljava/lang/Object;Ljava/lang/Object;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onTabSelected',
                '(Lcom/google/android/material/tabs/TabLayout$Tab;)V',
                'com/google/android/material/tabs/TabLayout$OnTabSelectedListener',
                'trackTabLayoutSelected',
                '(Ljava/lang/Object;Ljava/lang/Object;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'android/widget/Toolbar$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'android/support/v7/widget/Toolbar$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'androidx/appcompat/widget/Toolbar$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;IZ)V',
                'android/content/DialogInterface$OnMultiChoiceClickListener',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'android/widget/PopupMenu$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'androidx/appcompat/widget/PopupMenu$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'android/support/v7/widget/PopupMenu$OnMenuItemClickListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'com/google/android/material/navigation/NavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'android/support/design/widget/NavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'android/support/design/widget/BottomNavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addInterfaceMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'com/google/android/material/bottomnavigation/BottomNavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
    }

    static {
        addClassMethod(new DailyyogaMIITMethodCell(
                'performClick',
                '()Z',
                'androidx/appcompat/widget/ActionMenuPresenter$OverflowMenuButton',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                0, 1,
                [Opcodes.ALOAD]))

        addClassMethod(new DailyyogaMIITMethodCell(
                'performClick',
                '()Z',
                'android/support/v7/widget/ActionMenuPresenter$OverflowMenuButton',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                0, 1,
                [Opcodes.ALOAD]))

        addClassMethod(new DailyyogaMIITMethodCell(
                'performClick',
                '()Z',
                'android/widget/ActionMenuPresenter$OverflowMenuButton',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                0, 1,
                [Opcodes.ALOAD]))
    }

    static void addInterfaceMethod(DailyyogaMIITMethodCell sensorsAnalyticsMethodCell) {
        if (sensorsAnalyticsMethodCell != null) {
            INTERFACE_METHODS.put(sensorsAnalyticsMethodCell.parent + sensorsAnalyticsMethodCell.name + sensorsAnalyticsMethodCell.desc, sensorsAnalyticsMethodCell)
        }
    }

    static void addClassMethod(DailyyogaMIITMethodCell sensorsAnalyticsMethodCell) {
        if (sensorsAnalyticsMethodCell != null) {
            CLASS_METHODS.put(sensorsAnalyticsMethodCell.parent + sensorsAnalyticsMethodCell.name + sensorsAnalyticsMethodCell.desc, sensorsAnalyticsMethodCell)
        }
    }

    /**
     * Fragment中的方法
     */
    public final static HashMap<String, DailyyogaMIITMethodCell> FRAGMENT_METHODS = new HashMap<>()

    static {
        FRAGMENT_METHODS.put('onResume()V', new DailyyogaMIITMethodCell(
                'onResume',
                '()V',
                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                'trackFragmentResume',
                '(Ljava/lang/Object;)V',
                0, 1,
                [Opcodes.ALOAD]))
        FRAGMENT_METHODS.put('setUserVisibleHint(Z)V', new DailyyogaMIITMethodCell(
                'setUserVisibleHint',
                '(Z)V',
                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                'trackFragmentSetUserVisibleHint',
                '(Ljava/lang/Object;Z)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        FRAGMENT_METHODS.put('onHiddenChanged(Z)V', new DailyyogaMIITMethodCell(
                'onHiddenChanged',
                '(Z)V',
                '',
                'trackOnHiddenChanged',
                '(Ljava/lang/Object;Z)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        FRAGMENT_METHODS.put('onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V', new DailyyogaMIITMethodCell(
                'onViewCreated',
                '(Landroid/view/View;Landroid/os/Bundle;)V',
                '',
                'onFragmentViewCreated',
                '(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V',
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD]))
    }

    /**
     * android.gradle 3.2.1 版本中，针对 Lambda 表达式处理
     */

    public final static HashMap<String, DailyyogaMIITMethodCell> LAMBDA_METHODS = new HashMap<>()
    //lambda 参数优化取样
    public final static ArrayList<DailyyogaMIITMethodCell> SAMPLING_LAMBDA_METHODS = new ArrayList<>()
    static {
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'Landroid/view/View$OnClickListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        SAMPLING_LAMBDA_METHODS.add(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'Landroid/view/View$OnClickListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'Landroid/widget/CompoundButton$OnCheckedChangeListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'Landroid/widget/RatingBar$OnRatingBarChangeListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'Landroid/widget/RadioGroup$OnCheckedChangeListener;',
                'trackRadioGroup',
                '(Landroid/widget/RadioGroup;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        SAMPLING_LAMBDA_METHODS.add(new DailyyogaMIITMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'Landroid/widget/RadioGroup$OnCheckedChangeListener;',
                'trackRadioGroup',
                '(Landroid/widget/RadioGroup;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'Landroid/content/DialogInterface$OnClickListener;',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onItemClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'Landroid/widget/AdapterView$OnItemClickListener;',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        SAMPLING_LAMBDA_METHODS.add(new DailyyogaMIITMethodCell(
                'onItemClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'Landroid/widget/AdapterView$OnItemClickListener;',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'Landroid/widget/ExpandableListView$OnGroupClickListener;',
                'trackExpandableListViewOnGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'Landroid/widget/ExpandableListView$OnChildClickListener;',
                'trackExpandableListViewOnChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
                1, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onTabChanged',
                '(Ljava/lang/String;)V',
                'Landroid/widget/TabHost$OnTabChangeListener;',
                'trackTabHost',
                '(Ljava/lang/String;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'Lcom/google/android/material/navigation/NavigationView$OnNavigationItemSelectedListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/support/design/widget/NavigationView$OnNavigationItemSelectedListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/support/design/widget/BottomNavigationView$OnNavigationItemSelectedListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'Lcom/google/android/material/bottomnavigation/BottomNavigationView$OnNavigationItemSelectedListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/widget/Toolbar$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/support/v7/widget/Toolbar$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroidx/appcompat/widget/Toolbar$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;IZ)V',
                'Landroid/content/DialogInterface$OnMultiChoiceClickListener;',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/widget/PopupMenu$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroidx/appcompat/widget/PopupMenu$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))
        addLambdaMethod(new DailyyogaMIITMethodCell(
                'onMenuItemClick',
                '(Landroid/view/MenuItem;)Z',
                'Landroid/support/v7/widget/PopupMenu$OnMenuItemClickListener;',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                1, 1,
                [Opcodes.ALOAD]))

        // Todo: 扩展
    }

    static void addLambdaMethod(DailyyogaMIITMethodCell sensorsAnalyticsMethodCell) {
        if (sensorsAnalyticsMethodCell != null) {
            LAMBDA_METHODS.put(sensorsAnalyticsMethodCell.parent + sensorsAnalyticsMethodCell.name + sensorsAnalyticsMethodCell.desc, sensorsAnalyticsMethodCell)
        }
    }
}