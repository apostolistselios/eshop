export interface OrderItemResponseDto {
  productId: number;
  productBrand: string;
  productType: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

export interface OrderResponseDto {
  id: number;
  items: OrderItemResponseDto[];
  totalAmount: number;
}
