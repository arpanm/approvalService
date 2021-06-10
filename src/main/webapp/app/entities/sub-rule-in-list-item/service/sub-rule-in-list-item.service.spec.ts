import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISubRuleInListItem, SubRuleInListItem } from '../sub-rule-in-list-item.model';

import { SubRuleInListItemService } from './sub-rule-in-list-item.service';

describe('Service Tests', () => {
  describe('SubRuleInListItem Service', () => {
    let service: SubRuleInListItemService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubRuleInListItem;
    let expectedResult: ISubRuleInListItem | ISubRuleInListItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubRuleInListItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        strItem: 'AAAAAAA',
        decItem: 0,
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

      it('should create a SubRuleInListItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SubRuleInListItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubRuleInListItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            strItem: 'BBBBBB',
            decItem: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SubRuleInListItem', () => {
        const patchObject = Object.assign(
          {
            strItem: 'BBBBBB',
          },
          new SubRuleInListItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SubRuleInListItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            strItem: 'BBBBBB',
            decItem: 1,
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

      it('should delete a SubRuleInListItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubRuleInListItemToCollectionIfMissing', () => {
        it('should add a SubRuleInListItem to an empty array', () => {
          const subRuleInListItem: ISubRuleInListItem = { id: 123 };
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing([], subRuleInListItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subRuleInListItem);
        });

        it('should not add a SubRuleInListItem to an array that contains it', () => {
          const subRuleInListItem: ISubRuleInListItem = { id: 123 };
          const subRuleInListItemCollection: ISubRuleInListItem[] = [
            {
              ...subRuleInListItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing(subRuleInListItemCollection, subRuleInListItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SubRuleInListItem to an array that doesn't contain it", () => {
          const subRuleInListItem: ISubRuleInListItem = { id: 123 };
          const subRuleInListItemCollection: ISubRuleInListItem[] = [{ id: 456 }];
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing(subRuleInListItemCollection, subRuleInListItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subRuleInListItem);
        });

        it('should add only unique SubRuleInListItem to an array', () => {
          const subRuleInListItemArray: ISubRuleInListItem[] = [{ id: 123 }, { id: 456 }, { id: 24816 }];
          const subRuleInListItemCollection: ISubRuleInListItem[] = [{ id: 123 }];
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing(subRuleInListItemCollection, ...subRuleInListItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subRuleInListItem: ISubRuleInListItem = { id: 123 };
          const subRuleInListItem2: ISubRuleInListItem = { id: 456 };
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing([], subRuleInListItem, subRuleInListItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subRuleInListItem);
          expect(expectedResult).toContain(subRuleInListItem2);
        });

        it('should accept null and undefined values', () => {
          const subRuleInListItem: ISubRuleInListItem = { id: 123 };
          expectedResult = service.addSubRuleInListItemToCollectionIfMissing([], null, subRuleInListItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subRuleInListItem);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
