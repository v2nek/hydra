//
//  SchamperArticleViewControllerViewController.h
//  Hydra
//
//  Created by Pieter De Baets on 17/07/12.
//  Copyright (c) 2012 Zeus WPI. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SchamperArticle.h"

@interface WebViewController : UIViewController <UIWebViewDelegate>

@property (nonatomic, readonly) UIWebView *webView;
@property (nonatomic, strong) NSString *trackedViewName;

- (void)loadHtml:(NSString *)path;

@end
