//
//  RNWebsocketServer.m
//  RNWebsocketServer
//
//  Created by Unexus on 04/12/2017.
//  Copyright © 2017 Unexus. All rights reserved.
//

#import "RNWebsocketServer.h"
#import <PocketSocket/PSWebSocketServer.h>

@interface RNWebsocketServer () <PSWebSocketServerDelegate>

@property (nonatomic, strong) PSWebSocketServer *server;

@end


@implementation RNWebsocketServer

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(start: (NSString *) ipAddress
                  port: (int) port)
{
    self.server = [PSWebSocketServer serverWithHost:ipAddress port:port];
    self.server.delegate = self;
    
    [self.server start];
}

RCT_EXPORT_METHOD(stop)
{
    [self.server stop];
}

- (BOOL)server:(PSWebSocketServer *)server acceptWebSocketFrom:(NSData*)address withRequest:(NSURLRequest *)request trust:(SecTrustRef)trust response:(NSHTTPURLResponse **)response {
    NSLog(@"Websocket Request: %@", request);

    return YES;
}

#pragma mark - PSWebSocketServerDelegate

- (void)serverDidStart:(PSWebSocketServer *)server {
    NSLog(@"Websocket serverDidStart");
}
- (void)server:(PSWebSocketServer *)server didFailWithError:(NSError *)error {
    NSLog(@"Websocket didFailWithError");
    [NSException raise:NSInternalInconsistencyException format:error.localizedDescription];
}
- (void)serverDidStop:(PSWebSocketServer *)server {
    NSLog(@"Websocket serverDidStop");
}

- (void)server:(PSWebSocketServer *)server webSocketDidOpen:(PSWebSocket *)webSocket {
    NSLog(@"Websocket serverDidOpen");
}
- (void)server:(PSWebSocketServer *)server webSocket:(PSWebSocket *)webSocket didReceiveMessage:(id)message {
    NSMutableDictionary *dictionary = [NSJSONSerialization JSONObjectWithData:[message dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
    
    [dictionary setObject:[NSString stringWithFormat:@"%@", [webSocket remoteHost]] forKey:@"origin"];
    
    NSError *error;
    NSData *jsonObject = [NSJSONSerialization dataWithJSONObject:dictionary options:0 error:&error];
    
    for (PSWebSocket *connection in [server getWebsocketConnections]) {
        [connection send:[[NSString alloc] initWithData:jsonObject encoding:NSUTF8StringEncoding]];
    }
}
- (void)server:(PSWebSocketServer *)server webSocket:(PSWebSocket *)webSocket didFailWithError:(NSError *)error {
    
}
- (void)server:(PSWebSocketServer *)server webSocket:(PSWebSocket *)webSocket didCloseWithCode:(NSInteger)code reason:(NSString *)reason wasClean:(BOOL)wasClean {
    
}
@end
