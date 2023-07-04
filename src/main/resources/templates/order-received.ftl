<html>
<head>
    <style>
      body {
          display: grid;
          place-content: center;
      }

      td {
        font-weight: bold
      }

      .alnright { text-align: right; }

      .product { display: flex; }
      .product > * { padding-right: 10vw }
    </style>
</head>
<body>
    <h2>Thank you for choosing Tech E-Store!</h2>
    <p>
        Your order from ${date} has been successfully received.<br/>
        ID of the order: <b>${orderId}</b>
    </p>
    <h3>Order summary:</h3>
    <#list products as productWrapper>
        <div class="product">
            <img src="${productWrapper.product.mainImageUrl}" alt="Product image" style="width: 70px"/>
            <span>${productWrapper.product.name}</span>
            <span>${productWrapper.quantity} pcs</span>
            <span>${productWrapper.product.price} lv.</span>
        </div>
    </#list>
    <hr/>
    <table>
        <tr>
            <td>Total price</td>
            <td class="alnright">${totalPrice} lv.</td>
        </tr>
        <tr>
            <td>Full name</td>
            <td class="alnright">${fullName}</td>
        </tr>
        <tr>
            <td>Address</td>
            <td class="alnright">${address}</td>
        </tr>
        <tr>
            <td>Phone</td>
            <td class="alnright">${phone}</td>
        </tr>
        <tr>
            <td>Email</td>
            <td class="alnright"><a href="mailto:${email}">${email}</a></td>
        </tr>
    </table>
</body>
</html>