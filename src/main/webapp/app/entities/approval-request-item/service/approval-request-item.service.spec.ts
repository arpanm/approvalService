import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DataType } from 'app/entities/enumerations/data-type.model';
import { IApprovalRequestItem, ApprovalRequestItem } from '../approval-request-item.model';

import { ApprovalRequestItemService } from './approval-request-item.service';

describe('Service Tests', () => {
  describe('ApprovalRequestItem Service', () => {
    let service: ApprovalRequestItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IApprovalRequestItem;
    let expectedResult: IApprovalRequestItem | IApprovalRequestItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApprovalRequestItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fieldName: 'AAAAAAA',
        dataType: DataType.STR,
        strValue: 'AAAAAAA',
        decValue: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ApprovalRequestItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ApprovalRequestItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ApprovalRequestItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            dataType: 'BBBBBB',
            strValue: 'BBBBBB',
            decValue: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ApprovalRequestItem', () => {
        const patchObject = Object.assign(
          {
            fieldName: 'BBBBBB',
            dataType: 'BBBBBB',
            strValue: 'BBBBBB',
            decValue: 1,
          },
          new ApprovalRequestItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ApprovalRequestItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            dataType: 'BBBBBB',
            strValue: 'BBBBBB',
            decValue: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ApprovalRequestItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApprovalRequestItemToCollectionIfMissing', () => {
        it('should add a ApprovalRequestItem to an empty array', () => {
          const approvalRequestItem: IApprovalRequestItem = { id: 123 };
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing([], approvalRequestItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRequestItem);
        });

        it('should not add a ApprovalRequestItem to an array that contains it', () => {
          const approvalRequestItem: IApprovalRequestItem = { id: 123 };
          const approvalRequestItemCollection: IApprovalRequestItem[] = [
            {
              ...approvalRequestItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing(approvalRequestItemCollection, approvalRequestItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ApprovalRequestItem to an array that doesn't contain it", () => {
          const approvalRequestItem: IApprovalRequestItem = { id: 123 };
          const approvalRequestItemCollection: IApprovalRequestItem[] = [{ id: 456 }];
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing(approvalRequestItemCollection, approvalRequestItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRequestItem);
        });

        it('should add only unique ApprovalRequestItem to an array', () => {
          const approvalRequestItemArray: IApprovalRequestItem[] = [{ id: 123 }, { id: 456 }, { id: 74629 }];
          const approvalRequestItemCollection: IApprovalRequestItem[] = [{ id: 123 }];
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing(approvalRequestItemCollection, ...approvalRequestItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const approvalRequestItem: IApprovalRequestItem = { id: 123 };
          const approvalRequestItem2: IApprovalRequestItem = { id: 456 };
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing([], approvalRequestItem, approvalRequestItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRequestItem);
          expect(expectedResult).toContain(approvalRequestItem2);
        });

        it('should accept null and undefined values', () => {
          const approvalRequestItem: IApprovalRequestItem = { id: 123 };
          expectedResult = service.addApprovalRequestItemToCollectionIfMissing([], null, approvalRequestItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRequestItem);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
