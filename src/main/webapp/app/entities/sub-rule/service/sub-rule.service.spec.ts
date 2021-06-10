import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DataType } from 'app/entities/enumerations/data-type.model';
import { Condition } from 'app/entities/enumerations/condition.model';
import { AppendType } from 'app/entities/enumerations/append-type.model';
import { ISubRule, SubRule } from '../sub-rule.model';

import { SubRuleService } from './sub-rule.service';

describe('Service Tests', () => {
  describe('SubRule Service', () => {
    let service: SubRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubRule;
    let expectedResult: ISubRule | ISubRule[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubRuleService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fieldName: 'AAAAAAA',
        dataType: DataType.STR,
        condition: Condition.EQUAL,
        rangeMinValue: 0,
        rangeMaxValue: 0,
        equalStrValue: 'AAAAAAA',
        equalDecValue: 0,
        appendType: AppendType.START,
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

      it('should create a SubRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SubRule()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            dataType: 'BBBBBB',
            condition: 'BBBBBB',
            rangeMinValue: 1,
            rangeMaxValue: 1,
            equalStrValue: 'BBBBBB',
            equalDecValue: 1,
            appendType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SubRule', () => {
        const patchObject = Object.assign(
          {
            fieldName: 'BBBBBB',
            condition: 'BBBBBB',
            equalStrValue: 'BBBBBB',
            equalDecValue: 1,
          },
          new SubRule()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SubRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            dataType: 'BBBBBB',
            condition: 'BBBBBB',
            rangeMinValue: 1,
            rangeMaxValue: 1,
            equalStrValue: 'BBBBBB',
            equalDecValue: 1,
            appendType: 'BBBBBB',
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

      it('should delete a SubRule', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubRuleToCollectionIfMissing', () => {
        it('should add a SubRule to an empty array', () => {
          const subRule: ISubRule = { id: 123 };
          expectedResult = service.addSubRuleToCollectionIfMissing([], subRule);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subRule);
        });

        it('should not add a SubRule to an array that contains it', () => {
          const subRule: ISubRule = { id: 123 };
          const subRuleCollection: ISubRule[] = [
            {
              ...subRule,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubRuleToCollectionIfMissing(subRuleCollection, subRule);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SubRule to an array that doesn't contain it", () => {
          const subRule: ISubRule = { id: 123 };
          const subRuleCollection: ISubRule[] = [{ id: 456 }];
          expectedResult = service.addSubRuleToCollectionIfMissing(subRuleCollection, subRule);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subRule);
        });

        it('should add only unique SubRule to an array', () => {
          const subRuleArray: ISubRule[] = [{ id: 123 }, { id: 456 }, { id: 61380 }];
          const subRuleCollection: ISubRule[] = [{ id: 123 }];
          expectedResult = service.addSubRuleToCollectionIfMissing(subRuleCollection, ...subRuleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subRule: ISubRule = { id: 123 };
          const subRule2: ISubRule = { id: 456 };
          expectedResult = service.addSubRuleToCollectionIfMissing([], subRule, subRule2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subRule);
          expect(expectedResult).toContain(subRule2);
        });

        it('should accept null and undefined values', () => {
          const subRule: ISubRule = { id: 123 };
          expectedResult = service.addSubRuleToCollectionIfMissing([], null, subRule, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subRule);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
