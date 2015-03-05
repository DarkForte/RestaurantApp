#include "Socket.h"
#include<iostream>
using namespace std;

const int PORT = 12121;
const int MAX_CONNECT = 100;

int main()
{
    cout<<"Working..."<<endl;
	SocketServer socket(PORT, MAX_CONNECT);
	cout<<"Waiting for Client"<<endl;

	while(true)
	{
		Socket *s = socket.Accept();
		s->SendLine("Hello!");
	
		cout<<"Hello!"<<endl;
	}
	system("pause");
	return 0;
}