let dataA = {
    "name": "2",
    "nodeList": [
        {
            "id": "4161576879",
            "name": "开始节点",
            "type": "0",
            "left": "424px",
            "top": "69px",
            "ico": "el-icon-time",
            "state": "success",
            "condition": "3",
            "script": "(q>1)AND(q>4)"
        },
        {
            "id": "9393207162",
            "name": "结束节点",
            "type": "-1",
            "left": "448px",
            "top": "173px",
            "ico": "el-icon-odometer",
            "state": "success",
            "condition": "1",
            "script": "(q==1)AND(q>2)"
        }
    ],
    "lineList": [
        {
            "from": "4161576879",
            "to": "9393207162"
        }
    ]
}
export function getDataA () {
    return dataA
}
