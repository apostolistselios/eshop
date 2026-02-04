export interface AddCartItemDto {
  productId: number;
  quantity: number;
}

export interface UpdateCartItemDto {
  quantity: number;
}

export interface CartItemResponseDto {
  productId: number;
  productBrand: string;
  productType: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

export interface CartResponseDto {
  cartId: number;
  items: CartItemResponseDto[];
  totalAmount: number;
}

export interface OrderItemResponseDto {
  productId: number;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

export interface OrderResponseDto {
  id: number;
  items: OrderItemResponseDto[];
  totalAmount: number;
}
