#!/usr/bin/python  
#coding:utf-8
#author: duws
#time: 2019-05-16 13:00


import smtplib, time, os
from email.mime.text import MIMEText
from email.header import Header
 
def send_mail_html(file):
    '''发送html内容邮件'''
    # 发送邮箱
    #sender = 'l10880347@163.com'
    sender = ("%s<wenshu.du.ext@nokia-sbell.com>")%(Header('Zabbix监控系统告警!','utf-8'),) 
    # 接收邮箱
    receiver = 'wenshu.du.ext@nokia-sbell.com'
    # 发送邮件主题
    t = time.strftime("%Y年%m月%d日", time.localtime())
    subject = t +'zabbix监控报表！'
    # 发送邮箱服务器
    #smtpserver = 'smtp.163.com'
    smtpserver = 'mail.company.com'
    # 发送邮箱用户/密码
    username = 'wenshud'
    password = 'nokia#00000'
 
    # 读取html文件内容
    f = open(file, 'rb')
    mail_body = f.read()
    f.close()
 
    # 组装邮件内容和标题，中文需参数‘utf-8’，单字节字符不需要
    msg = MIMEText(mail_body, _subtype='html', _charset='utf-8')
    msg['Subject'] = Header(subject, 'utf-8')
    msg['From'] = sender
    msg['To'] = receiver
    # 登录并发送邮件
    try:
        smtp = smtplib.SMTP()
        smtp.connect(smtpserver)
        smtp.login(username, password)
        smtp.sendmail(sender, receiver, msg.as_string())
    except:
        print("邮件发送失败！")
    else:
        print("邮件发送成功！")
    finally:
        smtp.quit()
 
def find_new_file(dir):
    '''查找目录下最新的文件'''
    file_lists = os.listdir(dir)
    file_lists.sort(key=lambda fn: os.path.getmtime(dir + "/" + fn)
                    if not os.path.isdir(dir + "/" + fn)
                    else 0)
    # print('最新的文件为： ' + file_lists[-1])
    file = os.path.join(dir, file_lists[-1])
    print('/etc/zabbix/script/html/：', file)
    return file
 
dir = '/etc/zabbix/script/html/'  # 指定文件目录
file = find_new_file(dir)  # 查找最新的html文件
send_mail_html(file)  # 发送html内容邮件
